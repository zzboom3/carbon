package com.carbon.system.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carbon.common.api.Paging;
import com.carbon.domain.common.ApiResult;
import com.carbon.system.entity.SysRole;
import com.carbon.system.entity.SysRoleMenu;
import com.carbon.system.mapper.SysRoleMenuMapper;
import com.carbon.system.param.SysRoleMenuAddParam;
import com.carbon.system.param.SysRoleQueryParam;
import com.carbon.system.vo.SysRoleQueryVo;
import com.carbon.system.common.BaseController;
import com.carbon.system.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 角色  前端控制器
 * </p>
 *
 * @author Li Jun
 * @since 2021-06-18
 */
@Slf4j
@RestController
@RequestMapping("/sysRole")
@Api(value = "角色 模块", tags = {"角色 模块"})
public class SysRoleController extends BaseController {

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;

    @PostMapping("/getPageList")
    @ApiOperation(value = "获取角色分页列表", notes = "获取角色分页列表")
    public ApiResult<Paging<SysRole>> getPageList(@RequestBody(required = false) SysRoleQueryParam param) {
        SysRoleQueryParam query = param == null ? new SysRoleQueryParam() : param;
        long current = query.getCurrent() == null ? 1L : query.getCurrent();
        long size = query.getSize() == null ? 10L : query.getSize();

        Page<SysRole> page = sysRoleService.page(
                new Page<>(current, size),
                Wrappers.lambdaQuery(SysRole.class)
                        .like(query.getRoleName() != null && !query.getRoleName().trim().isEmpty(), SysRole::getRoleName, query.getRoleName())
                        .eq(query.getStatus() != null && !query.getStatus().trim().isEmpty(), SysRole::getStatus, query.getStatus())
                        .orderByDesc(SysRole::getId)
        );
        return ApiResult.ok(new Paging<>(page));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增角色", notes = "新增角色")
    public ApiResult<Boolean> add(@RequestBody SysRole role) {
        return ApiResult.ok(sysRoleService.save(role));
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改角色", notes = "修改角色")
    public ApiResult<Boolean> update(@RequestBody SysRole role) {
        return ApiResult.ok(sysRoleService.updateById(role));
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除角色", notes = "删除角色")
    public ApiResult<Boolean> delete(@PathVariable("id") Long id) {
        sysRoleMenuMapper.delete(Wrappers.lambdaQuery(SysRoleMenu.class).eq(SysRoleMenu::getRoleId, id));
        return ApiResult.ok(sysRoleService.removeById(id));
    }

    @GetMapping("/menu/{id}")
    @ApiOperation(value = "查询角色菜单", notes = "查询角色菜单")
    public ApiResult<List<SysRoleMenu>> getRoleMenu(@PathVariable("id") Long roleId) {
        List<SysRoleMenu> list = sysRoleMenuMapper.selectList(
                Wrappers.lambdaQuery(SysRoleMenu.class)
                        .eq(SysRoleMenu::getRoleId, roleId)
        );
        return ApiResult.ok(list);
    }

    @PostMapping("/menu/add")
    @ApiOperation(value = "保存角色菜单", notes = "保存角色菜单")
    public ApiResult<Boolean> addRoleMenu(@RequestBody SysRoleMenuAddParam param) {
        if (param == null || param.getRoleId() == null) {
            return ApiResult.fail("roleId不能为空");
        }
        Long roleId = param.getRoleId();

        sysRoleMenuMapper.delete(Wrappers.lambdaQuery(SysRoleMenu.class).eq(SysRoleMenu::getRoleId, roleId));
        List<Long> menuIds = param.getMenuIds() == null ? new ArrayList<>() : param.getMenuIds();
        for (Long menuId : menuIds) {
            if (menuId == null) continue;
            SysRoleMenu rm = new SysRoleMenu();
            rm.setRoleId(roleId);
            rm.setMenuId(menuId);
            sysRoleMenuMapper.insert(rm);
        }
        return ApiResult.ok(true);
    }
}
