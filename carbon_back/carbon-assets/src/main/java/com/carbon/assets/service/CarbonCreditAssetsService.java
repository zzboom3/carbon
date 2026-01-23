package com.carbon.assets.service;

import com.carbon.assets.entity.CarbonCreditAssets;
import com.carbon.assets.param.CarbonCreditAssetsAddParam;
import com.carbon.assets.vo.CarbonAssetsTotalVo;
import com.carbon.common.service.BaseService;
import com.carbon.assets.param.CarbonCreditAssetsQueryParam;
import com.carbon.assets.vo.CarbonCreditAssetsQueryVo;
import com.carbon.common.api.Paging;

import java.io.Serializable;

/**
 * <p>
 * 碳信用资产 服务类
 * </p>
 *
 * @author Li Jun
 * @since 2022-04-24
 */
public interface CarbonCreditAssetsService extends BaseService<CarbonCreditAssets> {

    CarbonAssetsTotalVo getAssetsTotal();

    Paging<CarbonCreditAssetsQueryVo> getCarbonCreditAssetsPageList(CarbonCreditAssetsQueryParam param);

    CarbonCreditAssetsQueryVo getCarbonCreditAssetsDetail(Long id);
}
