package com.carbon.assets.controller;

import com.carbon.assets.vo.CarbonAssetsTotalVo;
import com.carbon.assets.entity.CarbonCreditAssets;
import com.carbon.assets.common.BaseController;
import com.carbon.domain.common.ApiResult;
import com.carbon.common.api.Paging;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carbon.assets.param.CarbonCreditAssetsQueryParam;
import com.carbon.assets.service.CarbonCreditAssetsService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import javax.annotation.Resource;


/**
 * <p>
 * 碳信用资产 前端控制器
 * </p>
 *
 * @author Li Jun
 * @since 2022-04-24
 */
@Slf4j
@RestController
@RequestMapping("/carbonCreditAssets")
@Api(value = "碳信用资产模块", tags = {"碳信用资产模块"})
public class CarbonCreditAssetsController extends BaseController {

    @Resource
    private CarbonCreditAssetsService carbonCreditAssetsService;

    @GetMapping("/assetsTotal")
    @ApiOperation(value = "碳信用面板数据", notes = "碳信用面板数据")
    public ApiResult<CarbonAssetsTotalVo> assetsTotal() {
        CarbonAssetsTotalVo vo = new CarbonAssetsTotalVo();
        vo.setTotal(BigDecimal.ZERO);
        vo.setAvailableAmount(BigDecimal.ZERO);
        vo.setFrozenAmount(BigDecimal.ZERO);
        vo.setLockedAmount(BigDecimal.ZERO);
        return ApiResult.ok(vo);
    }

    @PostMapping("/getPageList")
    @ApiOperation(value = "碳信用分页列表", notes = "碳信用分页列表")
    public ApiResult<Paging<CarbonCreditAssets>> getPageList(@Valid @RequestBody(required = false) CarbonCreditAssetsQueryParam param) {
        CarbonCreditAssetsQueryParam query = param == null ? new CarbonCreditAssetsQueryParam() : param;
        Page<CarbonCreditAssets> page = carbonCreditAssetsService.page(
                new Page<>(query.getCurrent(), query.getSize()),
                Wrappers.lambdaQuery(CarbonCreditAssets.class)
                        .eq(StrUtil.isNotBlank(query.getAssetsStatus()), CarbonCreditAssets::getAssetsStatus, query.getAssetsStatus())
                        .eq(StrUtil.isNotBlank(query.getTransactionStatus()), CarbonCreditAssets::getTransactionStatus, query.getTransactionStatus())
                        .orderByDesc(CarbonCreditAssets::getId)
        );
        return ApiResult.ok(new Paging<>(page));
    }

    @GetMapping("/info/{id}")
    @ApiOperation(value = "碳信用详情", notes = "碳信用详情")
    public ApiResult<CarbonCreditAssets> info(@PathVariable("id") Long id) {
        return ApiResult.ok(carbonCreditAssetsService.getById(id));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增碳信用", notes = "新增碳信用")
    public ApiResult<Boolean> add(@Valid @RequestBody CarbonCreditAssets data) {
        return ApiResult.ok(carbonCreditAssetsService.save(data));
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新碳信用", notes = "更新碳信用")
    public ApiResult<Boolean> update(@Valid @RequestBody CarbonCreditAssets data) {
        return ApiResult.ok(carbonCreditAssetsService.updateById(data));
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除碳信用(兼容前端)", notes = "删除碳信用")
    public ApiResult<Boolean> deleteCompat(@RequestParam("id") Long id) {
        return ApiResult.ok(carbonCreditAssetsService.removeById(id));
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除碳信用", notes = "删除碳信用")
    public ApiResult<Boolean> delete(@PathVariable("id") Long id) {
        return ApiResult.ok(carbonCreditAssetsService.removeById(id));
    }
}
