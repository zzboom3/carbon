package com.carbon.trade.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.carbon.trade.entity.CarbonTradePrice;
import com.carbon.trade.param.CarbonTradePriceQueryParam;
import com.carbon.trade.vo.CarbonTradePriceQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 碳交易询报价 Mapper 接口
 * </p>
 *
 * @author lin rizhao
 * @since 2022-05-22
 */
@Repository
public interface CarbonTradePriceMapper extends BaseMapper<CarbonTradePrice> {

}
