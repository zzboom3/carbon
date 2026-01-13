package com.carbon.system.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.carbon.common.utils.HttpContextUtils;
import com.carbon.common.utils.JwtUtil;
import com.carbon.domain.common.ApiCode;
import com.carbon.domain.common.ApiResult;
import com.carbon.system.entity.SysMenu;
import com.carbon.system.entity.SysRoleMenu;
import com.carbon.system.mapper.SysRoleMapper;
import com.carbon.system.mapper.SysRoleMenuMapper;
import com.carbon.system.param.SysMenuQueryParam;
import com.carbon.system.param.SysMenuStatusParam;
import com.carbon.system.vo.SysMenuVo;
import com.carbon.system.common.BaseController;
import com.carbon.system.service.SysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;


/**
 * <p>
 * 菜单  前端控制器
 * </p>
 *
 * @author Li Jun
 * @since 2021-06-18
 */
@Slf4j
@RestController
@RequestMapping("/sysMenu")
@Api(value = "菜单 模块", tags = {"菜单 模块"})
public class SysMenuController extends BaseController {

    @Autowired
    private SysMenuService sysMenuService;

    @PostMapping("/userMenus")
    @ApiOperation(value = "获取当前登录人菜单列表", notes = "获取当前登录人菜单列表")
    public ApiResult<List<SysMenuVo>> userMenus() {
        if (!StrUtil.isNotBlank(HttpContextUtils.getRequestToken()) || !JwtUtil.isValidToken(HttpContextUtils.getRequestToken())) {
            return ApiResult.fail(ApiCode.UNAUTHORIZED);
        }
        return ApiResult.ok(sysMenuService.getCurrentUserMenus());
    }

    @PostMapping("/list")
    @ApiOperation(value = "查询菜单树列表", notes = "查询菜单树列表")
    public ApiResult<List<SysMenuVo>> getMenuList(@RequestBody(required = false) SysMenuQueryParam param) {
        return ApiResult.ok(sysMenuService.getMenuTree(param));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增菜单", notes = "新增菜单")
    public ApiResult<Boolean> addMenu(@RequestBody SysMenu menu) {
        return ApiResult.ok(sysMenuService.save(menu));
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改菜单", notes = "修改菜单")
    public ApiResult<Boolean> updateMenu(@RequestBody SysMenu menu) {
        return ApiResult.ok(sysMenuService.updateById(menu));
    }

    @PostMapping("/updateStatus")
    @ApiOperation(value = "修改菜单状态", notes = "修改菜单状态")
    public ApiResult<Boolean> updateMenuStatus(@RequestBody SysMenuStatusParam param) {
        if (param == null || param.getId() == null) {
            return ApiResult.fail("id不能为空");
        }
        SysMenu menu = new SysMenu();
        menu.setId(param.getId());
        menu.setStatus(param.getStatus());
        return ApiResult.ok(sysMenuService.updateById(menu));
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除菜单", notes = "删除菜单")
    public ApiResult<Boolean> deleteMenu(@PathVariable("id") Long id) {
        return ApiResult.ok(sysMenuService.removeById(id));
    }
}
