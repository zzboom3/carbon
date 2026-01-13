package com.carbon.trade.controller;

import com.carbon.common.api.Paging;
import com.carbon.domain.common.ApiResult;
import com.carbon.trade.common.BaseController;
import com.carbon.trade.entity.CarbonTradeQuote;
import com.carbon.trade.param.CarbonTradeQuoteQueryParam;
import com.carbon.trade.param.StartTradingParam;
import com.carbon.trade.service.CarbonTradeQuoteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.annotation.Resource;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;


/**
 * <p>
 * 碳交易供需行情 前端控制器
 * </p>
 *
 * @author lin rizhao
 * @since 2022-05-21
 */
@Slf4j
@RestController
@RequestMapping("/carbonTradeQuote")
@Api(value = "碳交易供需行情模块", tags = {"碳交易供需行情模块"})
public class CarbonTradeQuoteController extends BaseController {

    @Resource
    private CarbonTradeQuoteService carbonTradeQuoteService;

    @PostMapping("/getPageList")
    @ApiOperation(value = "供需行情分页列表", notes = "供需行情分页列表")
    public ApiResult<Paging<CarbonTradeQuote>> getPageList(@Valid @RequestBody(required = false) CarbonTradeQuoteQueryParam param) {
        CarbonTradeQuoteQueryParam query = param == null ? new CarbonTradeQuoteQueryParam() : param;
        Page<CarbonTradeQuote> page = carbonTradeQuoteService.page(
                new Page<>(query.getCurrent(), query.getSize()),
                Wrappers.lambdaQuery(CarbonTradeQuote.class)
                        .eq(StrUtil.isNotBlank(query.getTradeRole()), CarbonTradeQuote::getTradeRole, query.getTradeRole())
                        .eq(StrUtil.isNotBlank(query.getProjectScopeCode()), CarbonTradeQuote::getProjectScopeCode, query.getProjectScopeCode())
                        .eq(StrUtil.isNotBlank(query.getAssetType()), CarbonTradeQuote::getAssetType, query.getAssetType())
                        .eq(StrUtil.isNotBlank(query.getStatus()), CarbonTradeQuote::getStatus, query.getStatus())
                        .and(StrUtil.isNotBlank(query.getKeyword()), w -> w
                                .like(CarbonTradeQuote::getProjectName, query.getKeyword())
                                .or().like(CarbonTradeQuote::getInstitutionName, query.getKeyword())
                        )
                        .orderByDesc(CarbonTradeQuote::getId)
        );
        return ApiResult.ok(new Paging<>(page));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增供需行情", notes = "新增供需行情")
    public ApiResult<Boolean> add(@Valid @RequestBody CarbonTradeQuote data) {
        return ApiResult.ok(carbonTradeQuoteService.save(data));
    }

    @PostMapping("/startTrading")
    @ApiOperation(value = "发起询报价", notes = "发起询报价")
    public ApiResult<Boolean> startTrading(@Valid @RequestBody StartTradingParam param) {
        return ApiResult.ok(true);
    }

    @GetMapping("/search")
    @ApiOperation(value = "供需行情检索", notes = "供需行情检索")
    public ApiResult<List<CarbonTradeQuote>> search(@RequestParam(required = false) String keyword) {
        List<CarbonTradeQuote> list = carbonTradeQuoteService.list(
                Wrappers.lambdaQuery(CarbonTradeQuote.class)
                        .and(StrUtil.isNotBlank(keyword), w -> w
                                .like(CarbonTradeQuote::getProjectName, keyword)
                                .or().like(CarbonTradeQuote::getInstitutionName, keyword)
                        )
                        .orderByDesc(CarbonTradeQuote::getId)
                        .last("limit 50")
        );
        return ApiResult.ok(list);
    }
}

