package com.carbon.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.carbon.common.service.BaseServiceImpl;
import com.carbon.common.utils.HttpContextUtils;
import com.carbon.system.entity.SysAccountRole;
import com.carbon.system.entity.SysMenu;
import com.carbon.system.entity.SysRoleMenu;
import com.carbon.system.param.SysMenuQueryParam;
import com.carbon.system.param.SysMenuStatusParam;
import com.carbon.system.vo.SysMenuVo;
import com.carbon.system.mapper.SysMenuMapper;
import com.carbon.system.mapper.SysRoleMenuMapper;
import com.carbon.system.service.SysAccountRoleService;
import com.carbon.system.service.SysMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * <p>
 * 菜单  服务实现类
 * </p>
 *
 * @author Li Jun
 * @since 2021-06-18
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SysMenuServiceImpl extends BaseServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Value("${carbon.menu.showAll:false}")
    private boolean showAllMenus;

    @Resource
    private SysAccountRoleService sysAccountRoleService;

    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public List<SysMenuVo> getCurrentUserMenus() {
        if (showAllMenus) {
            List<SysMenu> menus = this.lambdaQuery()
                    .eq(SysMenu::getStatus, 0)
                    .list();
            return buildTree(menus);
        }

        Long accountId = HttpContextUtils.getAccountId();
        if (accountId == null) {
            return new ArrayList<>();
        }

        List<Long> roleIds = sysAccountRoleService.lambdaQuery()
                .select(SysAccountRole::getRoleId)
                .eq(SysAccountRole::getAccountId, accountId)
                .list()
                .stream()
                .map(SysAccountRole::getRoleId)
                .distinct()
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(roleIds)) {
            return new ArrayList<>();
        }

        List<Long> menuIds = sysRoleMenuMapper.selectList(
                        Wrappers.lambdaQuery(SysRoleMenu.class)
                                .select(SysRoleMenu::getMenuId)
                                .in(SysRoleMenu::getRoleId, roleIds)
                ).stream()
                .map(SysRoleMenu::getMenuId)
                .distinct()
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(menuIds)) {
            return new ArrayList<>();
        }

        Set<Long> allMenuIds = new HashSet<>(menuIds);
        Set<Long> pendingParentIds = new HashSet<>();

        List<SysMenu> initialMenus = this.listByIds(allMenuIds);
        for (SysMenu menu : initialMenus) {
            if (menu.getParentId() != null && menu.getParentId() > 0) {
                pendingParentIds.add(menu.getParentId());
            }
        }

        while (!pendingParentIds.isEmpty()) {
            Set<Long> needFetch = pendingParentIds.stream()
                    .filter(id -> !allMenuIds.contains(id))
                    .collect(Collectors.toSet());
            if (needFetch.isEmpty()) {
                break;
            }
            List<SysMenu> parents = this.listByIds(needFetch);
            for (SysMenu parent : parents) {
                allMenuIds.add(parent.getId());
                if (parent.getParentId() != null && parent.getParentId() > 0) {
                    pendingParentIds.add(parent.getParentId());
                }
            }
            pendingParentIds.removeAll(needFetch);
        }

        List<SysMenu> menus = this.lambdaQuery().in(SysMenu::getId, allMenuIds).list();
        return buildTree(menus);
    }

    @Override
    public List<SysMenuVo> getMenuTree(SysMenuQueryParam param) {
        SysMenuQueryParam query = param == null ? new SysMenuQueryParam() : param;

        List<SysMenu> menus = this.lambdaQuery()
                .eq(query.getHidden() != null, SysMenu::getHidden, query.getHidden())
                .eq(query.getStatus() != null, SysMenu::getStatus, query.getStatus())
                .like(query.getMenuName() != null && !query.getMenuName().trim().isEmpty(), SysMenu::getMenuName, query.getMenuName())
                .in(query.getMenuIds() != null && !query.getMenuIds().isEmpty(), SysMenu::getId, query.getMenuIds())
                .list();

        List<SysMenuVo> tree = buildTree(menus);
        if (query.getMenuLevel() == null) {
            return tree;
        }

        Integer level = query.getMenuLevel();
        List<SysMenuVo> filtered = new ArrayList<>();
        for (SysMenuVo node : tree) {
            if (node != null && level.equals(node.getMenuLevel())) {
                filtered.add(node);
            }
        }
        return filtered;
    }

    private List<SysMenuVo> buildTree(List<SysMenu> menus) {
        if (CollUtil.isEmpty(menus)) {
            return new ArrayList<>();
        }

        List<SysMenuVo> vos = menus.stream()
                .map(menu -> {
                    SysMenuVo vo = new SysMenuVo();
                    BeanUtil.copyProperties(menu, vo);
                    vo.setPermissionName(menu.getMenuName());
                    return vo;
                })
                .collect(Collectors.toList());

        Map<Long, SysMenuVo> byId = vos.stream()
                .collect(Collectors.toMap(SysMenuVo::getId, v -> v, (a, b) -> a));
        List<SysMenuVo> roots = new ArrayList<>();
        for (SysMenuVo vo : vos) {
            SysMenuVo parent = byId.get(vo.getParentId());
            if (parent == null || vo.getParentId() == null || vo.getParentId() == 0) {
                roots.add(vo);
            } else {
                parent.getChildren().add(vo);
            }
        }

        sortMenus(roots);
        return roots;
    }

    private void sortMenus(List<SysMenuVo> menus) {
        menus.sort(Comparator
                .comparing(SysMenuVo::getOrderNum, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(SysMenuVo::getId, Comparator.nullsLast(Comparator.naturalOrder())));
        for (SysMenuVo menu : menus) {
            if (CollUtil.isNotEmpty(menu.getChildren())) {
                sortMenus(menu.getChildren());
            }
        }
    }
}
