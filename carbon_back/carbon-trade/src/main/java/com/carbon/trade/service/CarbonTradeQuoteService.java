package com.carbon.trade.service;

import com.carbon.trade.entity.CarbonTradeQuote;
import com.carbon.common.service.BaseService;
import com.carbon.trade.param.CarbonTradeQuoteQueryParam;
import com.carbon.trade.param.StartTradingParam;
import com.carbon.trade.vo.CarbonTradeQuoteQueryVo;
import com.carbon.common.api.Paging;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 碳交易供需行情 服务类
 * </p>
 *
 * @author lin rizhao
 * @since 2022-05-21
 */
public interface CarbonTradeQuoteService extends BaseService<CarbonTradeQuote> {

    Paging<CarbonTradeQuote> getCarbonTradeQuotePageList(CarbonTradeQuoteQueryParam param);

    List<CarbonTradeQuote> searchByEs(String keyword, int pageNum, int pageSize);
}
