package com.carbon.trade.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carbon.common.exception.CommonBizException;
import com.carbon.common.redis.RedisService;
import com.carbon.domain.common.ApiResult;
import com.carbon.domain.common.constant.RedisKeyName;
import com.carbon.domain.system.api.SystemServiceApi;
import com.carbon.domain.system.vo.SysAccountModelVo;
import com.carbon.domain.system.vo.SysTenantModelVo;
import com.carbon.trade.common.enums.TradeRoleEnum;
import com.carbon.trade.common.enums.TradeStatusEnum;
import com.carbon.trade.entity.CarbonTradePrice;
import com.carbon.trade.entity.CarbonTradeQuote;
import com.carbon.trade.mapper.CarbonTradePriceMapper;
import com.carbon.trade.param.IntendedTransactionParam;
import com.carbon.trade.service.CarbonTradeContractService;
import com.carbon.trade.service.CarbonTradePriceService;
import com.carbon.trade.param.CarbonTradePriceQueryParam;
import com.carbon.trade.service.CarbonTradeQuoteService;
import com.carbon.trade.vo.CarbonTradePriceQueryVo;
import com.carbon.common.service.BaseServiceImpl;
import com.carbon.common.api.Paging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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

}
