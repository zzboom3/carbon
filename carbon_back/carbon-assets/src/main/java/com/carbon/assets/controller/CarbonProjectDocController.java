package com.carbon.assets.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carbon.assets.service.CarbonProjectDocService;
import com.carbon.assets.param.CarbonProjectDocQueryParam;
import com.carbon.assets.vo.CarbonProjectDocQueryVo;
import com.carbon.assets.entity.CarbonProjectDoc;
import com.carbon.assets.common.BaseController;
import com.carbon.domain.common.ApiResult;
import com.carbon.common.api.Paging;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.annotation.Resource;
import java.util.stream.Collectors;


/**
 * <p>
 * 碳减排项目文档 前端控制器
 * </p>
 *
 * @author Li Jun
 * @since 2022-04-24
 */
@Slf4j
@RestController
@RequestMapping("/carbonProjectDoc")
@Api(value = "碳减排项目文档模块", tags = {"碳减排项目文档模块"})
public class CarbonProjectDocController extends BaseController {

    @Resource
    private CarbonProjectDocService carbonProjectDocService;

    @PostMapping("/add")
    @ApiOperation(value = "新增项目文档", notes = "新增项目文档")
    public ApiResult<Boolean> add(@Valid @RequestBody CarbonProjectDoc doc) {
        return ApiResult.ok(carbonProjectDocService.save(doc));
    }

    @PostMapping("/getPageList")
    @ApiOperation(value = "项目文档分页列表", notes = "项目文档分页列表")
    public ApiResult<Paging<CarbonProjectDocQueryVo>> getPageList(@Valid @RequestBody(required = false) CarbonProjectDocQueryParam param) {
        CarbonProjectDocQueryParam query = param == null ? new CarbonProjectDocQueryParam() : param;
        Page<CarbonProjectDoc> page = carbonProjectDocService.page(
                new Page<>(query.getCurrent(), query.getSize()),
                Wrappers.lambdaQuery(CarbonProjectDoc.class)
                        .eq(query.getProjectId() != null, CarbonProjectDoc::getCarbonProjectId, query.getProjectId())
                        .eq(StrUtil.isNotBlank(query.getType()), CarbonProjectDoc::getType, query.getType())
                        .like(StrUtil.isNotBlank(query.getSearchKey()), CarbonProjectDoc::getTitle, query.getSearchKey())
                        .orderByDesc(CarbonProjectDoc::getId)
        );

        Page<CarbonProjectDocQueryVo> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(page.getRecords().stream().map(e -> {
            CarbonProjectDocQueryVo vo = new CarbonProjectDocQueryVo();
            BeanUtil.copyProperties(e, vo);
            return vo;
        }).collect(Collectors.toList()));

        return ApiResult.ok(new Paging<>(voPage));
    }

    @GetMapping("/info/{id}")
    @ApiOperation(value = "项目文档详情", notes = "项目文档详情")
    public ApiResult<CarbonProjectDocQueryVo> info(@PathVariable("id") Long id) {
        CarbonProjectDoc doc = carbonProjectDocService.getById(id);
        CarbonProjectDocQueryVo vo = new CarbonProjectDocQueryVo();
        if (doc != null) {
            BeanUtil.copyProperties(doc, vo);
        }
        return ApiResult.ok(vo);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除项目文档", notes = "删除项目文档")
    public ApiResult<Boolean> delete(@PathVariable("id") Long id) {
        return ApiResult.ok(carbonProjectDocService.removeById(id));
    }
}
