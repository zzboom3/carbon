package com.carbon.assets.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.carbon.assets.entity.ExchangeAccount;
import com.carbon.assets.param.ExchangeAccountQueryParam;
import com.carbon.assets.vo.ExchangeAccountInfo;
import com.carbon.assets.vo.ExchangeAccountQueryVo;
import com.carbon.assets.vo.CarbonExchangeAccountVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 交易账户  Mapper 接口
 * </p>
 *
 * @author Li Jun
 * @since 2021-08-16
 */
@Repository
public interface ExchangeAccountMapper extends BaseMapper<ExchangeAccount> {

}
