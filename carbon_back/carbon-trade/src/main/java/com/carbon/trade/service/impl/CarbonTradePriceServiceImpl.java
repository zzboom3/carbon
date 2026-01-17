package com.carbon.trade.service.impl;

import com.carbon.common.exception.CommonBizException;
import com.carbon.domain.common.ApiResult;
import com.carbon.domain.system.api.SystemServiceApi;
import com.carbon.domain.system.vo.SysAccountModelVo;
import com.carbon.domain.system.vo.SysTenantModelVo;
import com.carbon.trade.common.enums.TradeRoleEnum;
import com.carbon.trade.common.enums.TradeStatusEnum;
import com.carbon.trade.entity.CarbonTradeContract;
import com.carbon.trade.entity.CarbonTradePrice;
import com.carbon.trade.entity.CarbonTradeQuote;
import com.carbon.trade.mapper.CarbonTradePriceMapper;
import com.carbon.trade.param.IntendedTransactionParam;
import com.carbon.trade.param.StartTradingParam;
import com.carbon.trade.service.CarbonTradeContractService;
import com.carbon.trade.service.CarbonTradePriceService;
import com.carbon.trade.service.CarbonTradeQuoteService;
import com.carbon.common.service.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;


/**
 * <p>
 * 碳交易询报价 服务实现类
 * </p>
 *
 * @author lin rizhao
 * @since 2022-05-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class CarbonTradePriceServiceImpl extends BaseServiceImpl<CarbonTradePriceMapper, CarbonTradePrice> implements CarbonTradePriceService {

    @Autowired
    private CarbonTradeQuoteService carbonTradeQuoteService;

    @Autowired
    private CarbonTradeContractService carbonTradeContractService;

    @Autowired
    private SystemServiceApi systemServiceApi;

    @Override
    public void startTrading(StartTradingParam param) {
        if (param == null || param.getTradeQuoteId() == null) {
            throw new CommonBizException("tradeQuoteId不能为空");
        }
        Long tradeQuoteId;
        try {
            tradeQuoteId = Long.parseLong(String.valueOf(param.getTradeQuoteId()));
        } catch (Exception e) {
            throw new CommonBizException("tradeQuoteId不合法");
        }

        CarbonTradeQuote quote = carbonTradeQuoteService.getById(tradeQuoteId);
        if (quote == null) {
            throw new CommonBizException("交易行情不存在");
        }

        Long currentTenantId = getCurrentTenantId();
        if (currentTenantId == null) {
            throw new CommonBizException("请登录");
        }

        if (param.getTradeQuantity() == null || param.getTradeQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new CommonBizException("交易量不正确");
        }
        if (param.getAssetUnitPrice() == null || param.getAssetUnitPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new CommonBizException("单价不正确");
        }

        CarbonTradePrice price = new CarbonTradePrice();
        price.setTradeQuoteId(quote.getId());
        price.setProjectScopeCode(StrUtilBlankToDefault(quote.getProjectScopeCode(), param.getProjectScopeCode()));
        price.setProjectScope(StrUtilBlankToDefault(quote.getProjectScope(), param.getProjectScope()));

        if (TradeRoleEnum.SELLER.getStatus().equals(quote.getTradeRole())) {
            price.setBuyerId(currentTenantId);
            price.setBuyerTradeQuantity(param.getTradeQuantity());
            price.setBuyerUnitPrice(param.getAssetUnitPrice());
            price.setBuyerDeliveryMethod(param.getDeliveryMethod());
            price.setBuyerDeliveryExchange(param.getDeliveryExchange());
            price.setBuyerDeliveryTime(param.getDeliveryTime());

            price.setSellerId(quote.getPublisherId());
            price.setSellerTradeQuantity(quote.getTradeQuantity());
            price.setSellerUnitPrice(quote.getAssetUnitPrice());
            price.setSellerDeliveryMethod(quote.getDeliveryMethod());
            price.setSellerDeliveryExchange(quote.getDeliveryExchange());
            price.setSellerDeliveryTime(quote.getDeliveryTime());
        } else {
            price.setSellerId(currentTenantId);
            price.setSellerTradeQuantity(param.getTradeQuantity());
            price.setSellerUnitPrice(param.getAssetUnitPrice());
            price.setSellerDeliveryMethod(param.getDeliveryMethod());
            price.setSellerDeliveryExchange(param.getDeliveryExchange());
            price.setSellerDeliveryTime(param.getDeliveryTime());

            price.setBuyerId(quote.getPublisherId());
            price.setBuyerTradeQuantity(quote.getTradeQuantity());
            price.setBuyerUnitPrice(quote.getAssetUnitPrice());
            price.setBuyerDeliveryMethod(quote.getDeliveryMethod());
            price.setBuyerDeliveryExchange(quote.getDeliveryExchange());
            price.setBuyerDeliveryTime(quote.getDeliveryTime());
        }

        boolean ok = this.save(price);
        if (!ok) {
            throw new CommonBizException("发起询报价失败");
        }
    }

    @Override
    public void intendedTransaction(IntendedTransactionParam param) {
        if (param == null || param.getTradePriceId() == null) {
            throw new CommonBizException("tradePriceId不能为空");
        }
        Long tradePriceId;
        try {
            tradePriceId = Long.parseLong(String.valueOf(param.getTradePriceId()));
        } catch (Exception e) {
            throw new CommonBizException("tradePriceId不合法");
        }

        CarbonTradePrice price = this.getById(tradePriceId);
        if (price == null) {
            throw new CommonBizException("询报价不存在");
        }

        Long currentTenantId = getCurrentTenantId();
        if (currentTenantId == null) {
            throw new CommonBizException("请登录");
        }
        if (!Objects.equals(currentTenantId, price.getBuyerId()) && !Objects.equals(currentTenantId, price.getSellerId())) {
            throw new CommonBizException("无权限操作该询报价");
        }

        CarbonTradeQuote quote = carbonTradeQuoteService.getById(price.getTradeQuoteId());
        if (quote == null) {
            throw new CommonBizException("交易行情不存在");
        }

        CarbonTradeContract input = param.getTradeContract();
        if (input == null) {
            throw new CommonBizException("tradeContract不能为空");
        }

        CarbonTradeContract contract = new CarbonTradeContract();
        contract.setTradeQuoteId(quote.getId());
        contract.setBuyerId(price.getBuyerId());
        contract.setSellerId(price.getSellerId());
        contract.setAssetType(quote.getAssetType());
        contract.setProjectType(quote.getProjectType());
        contract.setAssetUnit(quote.getAssetUnit());

        BigDecimal tradeQuantity = input.getTradeQuantity();
        if (tradeQuantity == null || tradeQuantity.compareTo(BigDecimal.ZERO) <= 0) {
            tradeQuantity = firstNonNull(price.getBuyerTradeQuantity(), price.getSellerTradeQuantity());
        }
        if (tradeQuantity == null || tradeQuantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CommonBizException("交易量不正确");
        }
        contract.setTradeQuantity(tradeQuantity);

        BigDecimal unitPrice = input.getAssetUnitPrice();
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            unitPrice = firstNonNull(price.getBuyerUnitPrice(), price.getSellerUnitPrice());
        }
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CommonBizException("单价不正确");
        }
        contract.setAssetUnitPrice(unitPrice);

        contract.setDeliveryMethod(blankToDefault(input.getDeliveryMethod(), quote.getDeliveryMethod()));
        contract.setDeliveryExchange(blankToDefault(input.getDeliveryExchange(), quote.getDeliveryExchange()));
        contract.setDeliveryTime(input.getDeliveryTime() == null ? quote.getDeliveryTime() : input.getDeliveryTime());
        contract.setExpirationDate(input.getExpirationDate() == null ? quote.getExpirationDate() : input.getExpirationDate());
        contract.setRemark(input.getRemark());

        fillPartyInfo(contract);

        carbonTradeContractService.addTradeContract(contract);

        carbonTradeQuoteService.lambdaUpdate()
                .eq(CarbonTradeQuote::getId, quote.getId())
                .set(CarbonTradeQuote::getStatus, TradeStatusEnum.INTENDED_TRADE.getStatus())
                .update();
    }

    private void fillPartyInfo(CarbonTradeContract contract) {
        List<SysTenantModelVo> tenants = null;
        List<SysAccountModelVo> accounts = null;
        try {
            ApiResult<List<SysTenantModelVo>> tenantRt = systemServiceApi.getTenantList();
            if (tenantRt != null && tenantRt.getCode() == 200) {
                tenants = tenantRt.getData();
            }
        } catch (Exception ignored) {
        }
        try {
            ApiResult<List<SysAccountModelVo>> accountRt = systemServiceApi.getAccountList();
            if (accountRt != null && accountRt.getCode() == 200) {
                accounts = accountRt.getData();
            }
        } catch (Exception ignored) {
        }

        SysTenantModelVo buyerTenant = (tenants == null) ? null : tenants.stream().filter(t -> Objects.equals(t.getId(), contract.getBuyerId())).findFirst().orElse(null);
        SysTenantModelVo sellerTenant = (tenants == null) ? null : tenants.stream().filter(t -> Objects.equals(t.getId(), contract.getSellerId())).findFirst().orElse(null);

        SysAccountModelVo buyerAccount = (accounts == null) ? null : accounts.stream().filter(a -> Objects.equals(a.getTenantId(), contract.getBuyerId())).findFirst().orElse(null);
        SysAccountModelVo sellerAccount = (accounts == null) ? null : accounts.stream().filter(a -> Objects.equals(a.getTenantId(), contract.getSellerId())).findFirst().orElse(null);

        contract.setBuyerName(buyerTenant == null ? "" : buyerTenant.getTenantName());
        contract.setBuyerContacts(buyerTenant == null ? "" : buyerTenant.getContactsName());
        contract.setBuyerPhone(buyerTenant == null ? "" : buyerTenant.getContactsPhone());
        contract.setBuyerEmail(buyerTenant == null ? (buyerAccount == null ? "" : buyerAccount.getEmail()) : buyerTenant.getContactsEmail());

        contract.setSellerName(sellerTenant == null ? "" : sellerTenant.getTenantName());
        contract.setSellerContacts(sellerTenant == null ? "" : sellerTenant.getContactsName());
        contract.setSellerPhone(sellerTenant == null ? "" : sellerTenant.getContactsPhone());
        contract.setSellerEmail(sellerTenant == null ? (sellerAccount == null ? "" : sellerAccount.getEmail()) : sellerTenant.getContactsEmail());
    }

    private BigDecimal firstNonNull(BigDecimal a, BigDecimal b) {
        return a != null ? a : b;
    }

    private String blankToDefault(String a, String b) {
        return (a == null || a.trim().isEmpty()) ? (b == null ? "" : b) : a;
    }

    private String StrUtilBlankToDefault(String a, String b) {
        return (a == null || a.trim().isEmpty()) ? b : a;
    }
}
