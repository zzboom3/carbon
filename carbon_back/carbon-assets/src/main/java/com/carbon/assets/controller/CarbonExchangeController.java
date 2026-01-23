package com.carbon.assets.controller;

import cn.hutool.core.bean.BeanUtil;
import com.carbon.assets.param.CarbonExchangeQueryParam;
import com.carbon.assets.entity.CarbonExchange;
import com.carbon.assets.entity.ExchangeAccount;
import com.carbon.assets.common.BaseController;
import com.carbon.assets.service.CarbonExchangeService;
import com.carbon.assets.service.ExchangeAccountService;
import com.carbon.assets.vo.CarbonExchangeAccountVo;
import com.carbon.assets.vo.ExchangeAccountInfo;
import com.carbon.common.api.Paging;
import com.carbon.common.utils.HttpContextUtils;
import com.carbon.domain.common.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;


/**
 * <p>
 * 碳交易所  前端控制器
 * </p>
 *
 * @author Li Jun
 * @since 2021-08-16
 */
@Slf4j
@RestController
@RequestMapping("/carbonExchange")
@Api(value = "碳交易所 模块", tags = {"碳交易所 模块"})
public class CarbonExchangeController extends BaseController {

    @Resource
    private CarbonExchangeService carbonExchangeService;

    @Resource
    private ExchangeAccountService exchangeAccountService;

    @PostMapping("/getPageList")
    @ApiOperation(value = "交易所分页列表", notes = "交易所分页列表")
    public ApiResult<Paging<CarbonExchange>> getPageList(@Valid @RequestBody(required = false) CarbonExchangeQueryParam param) {
        CarbonExchangeQueryParam query = param == null ? new CarbonExchangeQueryParam() : param;
        Page<CarbonExchange> page = carbonExchangeService.page(
                new Page<>(query.getCurrent(), query.getSize()),
                Wrappers.lambdaQuery(CarbonExchange.class)
                        .like(StrUtil.isNotBlank(query.getSearchKey()), CarbonExchange::getName, query.getSearchKey())
                        .eq(query.getType() != null, CarbonExchange::getType, query.getType())
                        .eq(query.getStatus() != null, CarbonExchange::getStatus, query.getStatus())
                        .orderByDesc(CarbonExchange::getId)
        );
        return ApiResult.ok(new Paging<>(page));
    }

    @GetMapping("/info/{id}")
    @ApiOperation(value = "交易所详情", notes = "交易所详情")
    public ApiResult<CarbonExchange> info(@PathVariable("id") Long id) {
        return ApiResult.ok(carbonExchangeService.getById(id));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增交易所", notes = "新增交易所")
    public ApiResult<Boolean> add(@Valid @RequestBody CarbonExchange data) {
        return ApiResult.ok(carbonExchangeService.save(data));
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新交易所", notes = "更新交易所")
    public ApiResult<Boolean> update(@Valid @RequestBody CarbonExchange data) {
        return ApiResult.ok(carbonExchangeService.updateById(data));
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除交易所", notes = "删除交易所")
    public ApiResult<Boolean> delete(@PathVariable("id") Long id) {
        return ApiResult.ok(carbonExchangeService.removeById(id));
    }

    @PostMapping("/getListByTenant")
    @ApiOperation(value = "按租户获取交易所列表(兼容前端)", notes = "按租户获取交易所列表")
    public ApiResult<List<CarbonExchangeAccountVo>> getListByTenant(@RequestBody(required = false) Map<String, Object> param) {
        Long tenantId = HttpContextUtils.getTenantId();
        if (tenantId == null && param != null && param.get("tenantId") != null) {
            tenantId = Long.parseLong(String.valueOf(param.get("tenantId")));
        }

        List<CarbonExchange> exchanges = carbonExchangeService.list();
        List<ExchangeAccount> accounts = tenantId == null ? new ArrayList<>() : exchangeAccountService.list(
                Wrappers.lambdaQuery(ExchangeAccount.class)
                        .eq(ExchangeAccount::getTenantId, tenantId)
                        .orderByDesc(ExchangeAccount::getId)
        );

        Map<Long, List<ExchangeAccountInfo>> accountByExchangeId = accounts.stream()
                .map(a -> {
                    ExchangeAccountInfo info = new ExchangeAccountInfo();
                    BeanUtil.copyProperties(a, info);
                    return info;
                })
                .collect(Collectors.groupingBy(ExchangeAccountInfo::getCarbonExchangeId));

        List<CarbonExchangeAccountVo> result = new ArrayList<>();
        for (CarbonExchange e : exchanges) {
            CarbonExchangeAccountVo vo = new CarbonExchangeAccountVo();
            vo.setId(e.getId());
            vo.setExchangeName(e.getName());
            vo.setAccountInfoList(accountByExchangeId.getOrDefault(e.getId(), new ArrayList<>()));
            result.add(vo);
        }
        return ApiResult.ok(result);
    }
}
