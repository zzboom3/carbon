package com.carbon.trade.controller;

import com.carbon.trade.param.IntendedTransactionParam;
import com.carbon.trade.service.CarbonTradePriceService;
import com.carbon.trade.param.CarbonTradePriceQueryParam;
import com.carbon.trade.entity.CarbonTradePrice;
import com.carbon.trade.common.BaseController;
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
 * 碳交易询报价 前端控制器
 * </p>
 *
 * @author lin rizhao
 * @since 2022-05-22
 */
@Slf4j
@RestController
@RequestMapping("/carbonTradePrice")
@Api(value = "碳交易询报价模块", tags = {"碳交易询报价模块"})
public class CarbonTradePriceController extends BaseController {

    @Resource
    private CarbonTradePriceService carbonTradePriceService;

    @PostMapping("/getPageList")
    @ApiOperation(value = "询报价分页列表", notes = "询报价分页列表")
    public ApiResult<Paging<CarbonTradePrice>> getPageList(@Valid @RequestBody(required = false) CarbonTradePriceQueryParam param) {
        CarbonTradePriceQueryParam query = param == null ? new CarbonTradePriceQueryParam() : param;
        Page<CarbonTradePrice> page = carbonTradePriceService.page(
                new Page<>(query.getCurrent(), query.getSize()),
                Wrappers.lambdaQuery(CarbonTradePrice.class)
                        .eq(StrUtil.isNotBlank(query.getProjectScopeCode()), CarbonTradePrice::getProjectScopeCode, query.getProjectScopeCode())
                        .orderByDesc(CarbonTradePrice::getId)
        );
        return ApiResult.ok(new Paging<>(page));
    }

    @PostMapping("/info/{id}")
    @ApiOperation(value = "询报价详情(POST)", notes = "询报价详情")
    public ApiResult<CarbonTradePrice> infoPost(@PathVariable("id") Long id) {
        return ApiResult.ok(carbonTradePriceService.getById(id));
    }

    @GetMapping("/info/{id}")
    @ApiOperation(value = "询报价详情", notes = "询报价详情")
    public ApiResult<CarbonTradePrice> info(@PathVariable("id") Long id) {
        return ApiResult.ok(carbonTradePriceService.getById(id));
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新询报价(POST)", notes = "更新询报价")
    public ApiResult<Boolean> updatePost(@Valid @RequestBody CarbonTradePrice data) {
        return ApiResult.ok(carbonTradePriceService.updateById(data));
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新询报价", notes = "更新询报价")
    public ApiResult<Boolean> update(@Valid @RequestBody CarbonTradePrice data) {
        return ApiResult.ok(carbonTradePriceService.updateById(data));
    }

    @PostMapping("/intendedTransaction")
    @ApiOperation(value = "意向成交", notes = "意向成交")
    public ApiResult<Boolean> intendedTransaction(@Valid @RequestBody IntendedTransactionParam param) {
        carbonTradePriceService.intendedTransaction(param);
        return ApiResult.ok(true);
    }
}
