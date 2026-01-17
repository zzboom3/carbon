package com.carbon.assets.service.impl;

import com.carbon.assets.entity.CarbonQuotaAssets;
import com.carbon.assets.mapper.CarbonQuotaAssetsMapper;
import com.carbon.assets.service.CarbonQuotaAssetsService;
import com.carbon.assets.vo.CarbonAssetsTotalVo;
import com.carbon.common.service.BaseServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;


/**
 * <p>
 * 碳配额资产 服务实现类
 * </p>
 *
 * @author Li Jun
 * @since 2022-04-24
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class CarbonQuotaAssetsServiceImpl extends BaseServiceImpl<CarbonQuotaAssetsMapper, CarbonQuotaAssets> implements CarbonQuotaAssetsService {

    @Override
    public CarbonAssetsTotalVo getAssetsTotal() {
        List<CarbonQuotaAssets> list = this.list(
                Wrappers.lambdaQuery(CarbonQuotaAssets.class)
                        .select(
                                CarbonQuotaAssets::getTotal,
                                CarbonQuotaAssets::getAvailableAmount,
                                CarbonQuotaAssets::getFrozenAmount,
                                CarbonQuotaAssets::getLockedAmount
                        )
        );

        CarbonAssetsTotalVo vo = new CarbonAssetsTotalVo();
        vo.setTotal(sum(list, CarbonQuotaAssets::getTotal));
        vo.setAvailableAmount(sum(list, CarbonQuotaAssets::getAvailableAmount));
        vo.setFrozenAmount(sum(list, CarbonQuotaAssets::getFrozenAmount));
        vo.setLockedAmount(sum(list, CarbonQuotaAssets::getLockedAmount));
        return vo;
    }

    private BigDecimal sum(List<CarbonQuotaAssets> list, java.util.function.Function<CarbonQuotaAssets, BigDecimal> getter) {
        if (list == null || list.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return list.stream()
                .map(getter)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
