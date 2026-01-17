package com.carbon.assets.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.carbon.assets.entity.CarbonCreditAssets;
import com.carbon.assets.mapper.CarbonCreditAssetsMapper;
import com.carbon.assets.service.CarbonCreditAssetsService;
import com.carbon.assets.vo.CarbonAssetsTotalVo;
import com.carbon.common.service.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;


/**
 * <p>
 * 碳信用资产 服务实现类
 * </p>
 *
 * @author Li Jun
 * @since 2022-04-24
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class CarbonCreditAssetsServiceImpl extends BaseServiceImpl<CarbonCreditAssetsMapper, CarbonCreditAssets> implements CarbonCreditAssetsService {

    @Override
    public CarbonAssetsTotalVo getAssetsTotal() {
        List<CarbonCreditAssets> list = this.list(
                Wrappers.lambdaQuery(CarbonCreditAssets.class)
                        .select(
                                CarbonCreditAssets::getTotal,
                                CarbonCreditAssets::getAvailableAmount,
                                CarbonCreditAssets::getFrozenAmount,
                                CarbonCreditAssets::getLockedAmount
                        )
        );

        CarbonAssetsTotalVo vo = new CarbonAssetsTotalVo();
        vo.setTotal(sum(list, CarbonCreditAssets::getTotal));
        vo.setAvailableAmount(sum(list, CarbonCreditAssets::getAvailableAmount));
        vo.setFrozenAmount(sum(list, CarbonCreditAssets::getFrozenAmount));
        vo.setLockedAmount(sum(list, CarbonCreditAssets::getLockedAmount));
        return vo;
    }

    private BigDecimal sum(List<CarbonCreditAssets> list, java.util.function.Function<CarbonCreditAssets, BigDecimal> getter) {
        if (list == null || list.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return list.stream()
                .map(getter)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
