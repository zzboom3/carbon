package com.carbon.assets.controller;

import com.carbon.domain.assets.vo.MethodologyUploadParam;
import com.carbon.assets.service.CarbonMethodologyService;
import com.carbon.assets.param.CarbonMethodologyQueryParam;
import com.carbon.assets.entity.CarbonMethodology;
import com.carbon.assets.common.BaseController;
import com.carbon.domain.common.ApiResult;
import com.carbon.common.api.Paging;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.annotation.Resource;


/**
 * <p>
 * 碳减排方法学 前端控制器
 * </p>
 *
 * @author Li Jun
 * @since 2021-07-31
 */
@Slf4j
@RestController
@RequestMapping("/carbonMethodology")
@Api(value = "碳减排方法学模块", tags = {"碳减排方法学模块"})
public class CarbonMethodologyController extends BaseController {

    @Resource
    private CarbonMethodologyService carbonMethodologyService;

    @PostMapping("/getPageList")
    @ApiOperation(value = "方法学分页列表", notes = "方法学分页列表")
    public ApiResult<Paging<CarbonMethodology>> getPageList(@Valid @RequestBody(required = false) CarbonMethodologyQueryParam param) {
        CarbonMethodologyQueryParam query = param == null ? new CarbonMethodologyQueryParam() : param;
        String statusCode = StrUtil.isNotBlank(query.getStatusCode()) ? query.getStatusCode() : query.getStatus();
        Page<CarbonMethodology> page = carbonMethodologyService.page(
                new Page<>(query.getCurrent(), query.getSize()),
                Wrappers.lambdaQuery(CarbonMethodology.class)
                        .like(StrUtil.isNotBlank(query.getSearchKey()), CarbonMethodology::getName, query.getSearchKey())
                        .eq(StrUtil.isNotBlank(statusCode), CarbonMethodology::getStatusCode, statusCode)
                        .eq(StrUtil.isNotBlank(query.getFieldCode()), CarbonMethodology::getFieldCode, query.getFieldCode())
                        .eq(StrUtil.isNotBlank(query.getFieldChildCode()), CarbonMethodology::getFieldChildCode, query.getFieldChildCode())
                        .eq(StrUtil.isNotBlank(query.getIndustryCode()), CarbonMethodology::getIndustryCode, query.getIndustryCode())
                        .eq(StrUtil.isNotBlank(query.getCertificationCriteria()), CarbonMethodology::getCertificationCriteria, query.getCertificationCriteria())
                        .orderByDesc(CarbonMethodology::getId)
        );
        return ApiResult.ok(new Paging<>(page));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增方法学", notes = "新增方法学")
    public ApiResult<Boolean> add(@Valid @RequestBody CarbonMethodology data) {
        return ApiResult.ok(carbonMethodologyService.save(data));
    }

    @PostMapping("/upload")
    @ApiOperation(value = "上传方法学(兼容前端)", notes = "上传方法学")
    public ApiResult<Boolean> upload(@Valid @RequestBody MethodologyUploadParam param) {
        CarbonMethodology m = new CarbonMethodology();
        m.setName(param.getName());
        m.setMethodCode(param.getMethodCode());
        m.setDictCode(param.getDictCode());
        m.setFieldCode(param.getFieldCode());
        m.setIndustryCode(param.getIndustryCode());
        m.setCertificationCriteria(param.getCertificationCriteria());
        m.setSourceFileUrl(StrUtil.isNotBlank(param.getPdfUrl()) ? param.getPdfUrl() : param.getWordUrl());
        return ApiResult.ok(carbonMethodologyService.save(m));
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新方法学", notes = "更新方法学")
    public ApiResult<Boolean> update(@Valid @RequestBody CarbonMethodology data) {
        return ApiResult.ok(carbonMethodologyService.updateById(data));
    }

    @GetMapping("/info/{id}")
    @ApiOperation(value = "方法学详情", notes = "方法学详情")
    public ApiResult<CarbonMethodology> info(@PathVariable("id") Long id) {
        return ApiResult.ok(carbonMethodologyService.getById(id));
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除方法学", notes = "删除方法学")
    public ApiResult<Boolean> delete(@PathVariable("id") Long id) {
        return ApiResult.ok(carbonMethodologyService.removeById(id));
    }
}
