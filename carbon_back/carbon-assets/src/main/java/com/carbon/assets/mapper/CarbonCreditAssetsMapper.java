package com.carbon.assets.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.carbon.assets.entity.CarbonCreditAssets;
import com.carbon.assets.param.CarbonCreditAssetsQueryParam;
import com.carbon.assets.vo.CarbonAssetsTotalVo;
import com.carbon.assets.vo.CarbonCreditAssetsQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


/**
 * <p>
 * 碳信用资产 Mapper 接口
 * </p>
 *
 * @author Li Jun
 * @since 2022-04-24
 */
@Repository
public interface CarbonCreditAssetsMapper extends BaseMapper<CarbonCreditAssets> {

    IPage<CarbonCreditAssetsQueryVo> getCarbonCreditAssetsPageList(Page<?> page, @Param("param") CarbonCreditAssetsQueryParam param);

    CarbonCreditAssetsQueryVo getCarbonCreditAssetsById(@Param("id") Long id);

}
