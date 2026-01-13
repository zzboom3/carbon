package com.carbon.trade.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carbon.common.exception.CommonBizException;
import com.carbon.domain.common.constant.RocketDelayLevelConstant;
import com.carbon.domain.common.constant.RocketMqName;
import com.carbon.domain.trade.vo.MqCarbonTradeQuote;
import com.carbon.trade.common.enums.TradeRoleEnum;
import com.carbon.trade.common.enums.TradeStatusEnum;
import com.carbon.trade.entity.CarbonTradePrice;
import com.carbon.trade.entity.CarbonTradeQuote;
import com.carbon.trade.mapper.CarbonTradeQuoteMapper;
import com.carbon.trade.param.StartTradingParam;
import com.carbon.trade.repository.EsCarbonTradeQuoteRepository;
import com.carbon.trade.service.CarbonTradePriceService;
import com.carbon.trade.service.CarbonTradeQuoteService;
import com.carbon.trade.param.CarbonTradeQuoteQueryParam;
import com.carbon.trade.vo.CarbonTradeQuoteQueryVo;
import com.carbon.common.service.BaseServiceImpl;
import com.carbon.common.api.Paging;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.util.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * <p>
 * 碳交易供需行情 服务实现类
 * </p>
 *
 * @author lin rizhao
 * @since 2022-05-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class CarbonTradeQuoteServiceImpl extends BaseServiceImpl<CarbonTradeQuoteMapper, CarbonTradeQuote> implements CarbonTradeQuoteService {

    @Override
    public Paging<CarbonTradeQuote> getCarbonTradeQuotePageList(CarbonTradeQuoteQueryParam param) {
        CarbonTradeQuoteQueryParam query = param == null ? new CarbonTradeQuoteQueryParam() : param;
        Page<CarbonTradeQuote> page = this.page(
                new Page<>(query.getCurrent(), query.getSize()),
                Wrappers.lambdaQuery(CarbonTradeQuote.class)
                        .eq(StringUtils.hasText(query.getProjectScopeCode()), CarbonTradeQuote::getProjectScopeCode, query.getProjectScopeCode())
                        .eq(StringUtils.hasText(query.getTradeStatus()), CarbonTradeQuote::getStatus, query.getTradeStatus())
                        .and(StringUtils.hasText(query.getKeyword()), w -> w
                                .like(CarbonTradeQuote::getProjectName, query.getKeyword())
                                .or().like(CarbonTradeQuote::getInstitutionName, query.getKeyword())
                        )
        );
        return new Paging<>(page);
    }

    @Override
    public List<CarbonTradeQuote> searchByEs(String keyword, int pageNum, int pageSize) {
        int current = Math.max(pageNum, 1);
        int size = Math.max(pageSize, 1);
        Page<CarbonTradeQuote> page = this.page(
                new Page<>(current, size),
                Wrappers.lambdaQuery(CarbonTradeQuote.class)
                        .and(StringUtils.hasText(keyword), w -> w
                                .like(CarbonTradeQuote::getProjectName, keyword)
                                .or().like(CarbonTradeQuote::getInstitutionName, keyword)
                        )
                        .orderByDesc(CarbonTradeQuote::getId)
        );
        return page.getRecords();
    }
}
