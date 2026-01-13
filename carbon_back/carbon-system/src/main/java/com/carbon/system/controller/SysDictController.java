package com.carbon.system.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carbon.domain.system.vo.SysDictModelVo;
import com.carbon.system.entity.SysDictItem;
import com.carbon.system.param.SysDictAddParam;
import com.carbon.system.param.SysDictItemAddParam;
import com.carbon.system.param.SysDictItemQueryParam;
import com.carbon.system.service.SysDictItemService;
import com.carbon.system.service.SysDictService;
import com.carbon.system.param.SysDictQueryParam;
import com.carbon.system.vo.SysDictItemQueryVo;
import com.carbon.system.vo.SysDictQueryVo;
import com.carbon.system.entity.SysDict;
import com.carbon.system.common.BaseController;
import com.carbon.domain.common.ApiResult;
import com.carbon.common.api.Paging;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import java.util.stream.Collectors;


/**
 * <p>
 * 系统字典  前端控制器
 * </p>
 *
 * @author Li Jun
 * @since 2021-08-16
 */
@Slf4j
@RestController
@RequestMapping("/sysDict")
@Api(value = "系统字典 模块", tags = {"系统字典 模块"})
public class SysDictController extends BaseController {

    @Resource
    private SysDictService sysDictService;

    @Resource
    private SysDictItemService sysDictItemService;

    @GetMapping("/getAllDict")
    @ApiOperation(value = "获取所有字典", notes = "获取所有字典")
    public ApiResult<List<SysDictModelVo>> getAllDict() {
        return ApiResult.ok(sysDictService.getAllDict());
    }

    @GetMapping("/getCityDict")
    @ApiOperation(value = "获取城市字典", notes = "获取城市字典")
    public ApiResult<List<SysDictItem>> getCityDict() {
        List<SysDictItem> list = sysDictItemService.list(
                Wrappers.lambdaQuery(SysDictItem.class)
                        .eq(SysDictItem::getDictCode, "City")
                        .orderByAsc(SysDictItem::getSortOrder)
        );
        return ApiResult.ok(list);
    }

    @PostMapping("/getPageList")
    @ApiOperation(value = "字典分页列表", notes = "字典分页列表")
    public ApiResult<Paging<SysDictQueryVo>> getPageList(@RequestBody(required = false) SysDictQueryParam param) {
        SysDictQueryParam query = param == null ? new SysDictQueryParam() : param;
        Page<SysDict> page = sysDictService.page(
                new Page<>(query.getCurrent(), query.getSize()),
                Wrappers.lambdaQuery(SysDict.class)
                        .like(StrUtil.isNotBlank(query.getDictCode()), SysDict::getDictCode, query.getDictCode())
                        .like(StrUtil.isNotBlank(query.getDictName()), SysDict::getDictName, query.getDictName())
                        .orderByDesc(SysDict::getId)
        );
        Page<SysDictQueryVo> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(page.getRecords().stream().map(e -> {
            SysDictQueryVo vo = new SysDictQueryVo();
            BeanUtil.copyProperties(e, vo);
            return vo;
        }).collect(Collectors.toList()));
        return ApiResult.ok(new Paging<>(voPage));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增字典", notes = "新增字典")
    public ApiResult<Boolean> addDict(@RequestBody SysDict dict) {
        return ApiResult.ok(sysDictService.save(dict));
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改字典", notes = "修改字典")
    public ApiResult<Boolean> updateDict(@RequestBody SysDict dict) {
        return ApiResult.ok(sysDictService.updateById(dict));
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除字典", notes = "删除字典")
    public ApiResult<Boolean> deleteDict(@PathVariable("id") Long id) {
        return ApiResult.ok(sysDictService.removeById(id));
    }

    @PostMapping("/dictConfig/getPageList")
    @ApiOperation(value = "字典项分页列表", notes = "字典项分页列表")
    public ApiResult<Paging<SysDictItemQueryVo>> getDictItemPageList(@Valid @RequestBody SysDictItemQueryParam param) {
        return ApiResult.ok(sysDictItemService.getSysDictItemPageList(param));
    }

    @PostMapping("/dictConfig/add")
    @ApiOperation(value = "新增字典项", notes = "新增字典项")
    public ApiResult<Boolean> addDictItem(@Valid @RequestBody SysDictItemAddParam param) {
        SysDictItem item = new SysDictItem();
        BeanUtil.copyProperties(param, item);
        return ApiResult.ok(sysDictItemService.save(item));
    }

    @PutMapping("/dictConfig/update")
    @ApiOperation(value = "修改字典项", notes = "修改字典项")
    public ApiResult<Boolean> updateDictItem(@Valid @RequestBody SysDictItemAddParam param) {
        SysDictItem item = new SysDictItem();
        BeanUtil.copyProperties(param, item);
        return ApiResult.ok(sysDictItemService.updateById(item));
    }

    @DeleteMapping("/dictConfig/delete/{id}")
    @ApiOperation(value = "删除字典项", notes = "删除字典项")
    public ApiResult<Boolean> deleteDictItem(@PathVariable("id") Long id) {
        return ApiResult.ok(sysDictItemService.removeById(id));
    }
}
