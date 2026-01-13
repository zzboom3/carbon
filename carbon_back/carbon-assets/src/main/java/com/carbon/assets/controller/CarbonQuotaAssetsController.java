package com.carbon.assets.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carbon.assets.service.CarbonQuotaAssetsService;
import com.carbon.assets.param.CarbonQuotaAssetsQueryParam;
import com.carbon.assets.vo.CarbonAssetsTotalVo;
import com.carbon.assets.vo.CarbonQuotaAssetsQueryVo;
import com.carbon.assets.entity.CarbonQuotaAssets;
import com.carbon.assets.common.BaseController;
import com.carbon.domain.common.ApiResult;
import com.carbon.common.api.Paging;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import javax.annotation.Resource;


/**
 * <p>
 * 碳配额资产 前端控制器
 * </p>
 *
 * @author Li Jun
 * @since 2022-04-24
 */
@Slf4j
@RestController
@RequestMapping("/carbonQuotaAssets")
@Api(value = "碳配额资产模块", tags = {"碳配额资产模块"})
public class CarbonQuotaAssetsController extends BaseController {

    @Resource
    private CarbonQuotaAssetsService carbonQuotaAssetsService;

    @GetMapping("/assetsTotal")
    @ApiOperation(value = "碳配额面板数据", notes = "碳配额面板数据")
    public ApiResult<CarbonAssetsTotalVo> assetsTotal() {
        CarbonAssetsTotalVo vo = new CarbonAssetsTotalVo();
        vo.setTotal(BigDecimal.ZERO);
        vo.setAvailableAmount(BigDecimal.ZERO);
        vo.setFrozenAmount(BigDecimal.ZERO);
        vo.setLockedAmount(BigDecimal.ZERO);
        return ApiResult.ok(vo);
    }

    @PostMapping("/getPageList")
    @ApiOperation(value = "碳配额分页列表", notes = "碳配额分页列表")
    public ApiResult<Paging<CarbonQuotaAssets>> getPageList(@Valid @RequestBody(required = false) CarbonQuotaAssetsQueryParam param) {
        CarbonQuotaAssetsQueryParam query = param == null ? new CarbonQuotaAssetsQueryParam() : param;
        Page<CarbonQuotaAssets> page = carbonQuotaAssetsService.page(
                new Page<>(query.getCurrent(), query.getSize()),
                Wrappers.lambdaQuery(CarbonQuotaAssets.class)
                        .like(StrUtil.isNotBlank(query.getAgencyName()), CarbonQuotaAssets::getAgencyName, query.getAgencyName())
                        .orderByDesc(CarbonQuotaAssets::getId)
        );
        return ApiResult.ok(new Paging<>(page));
    }

    @GetMapping("/info/{id}")
    @ApiOperation(value = "碳配额详情", notes = "碳配额详情")
    public ApiResult<CarbonQuotaAssets> info(@PathVariable("id") Long id) {
        return ApiResult.ok(carbonQuotaAssetsService.getById(id));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增碳配额", notes = "新增碳配额")
    public ApiResult<Boolean> add(@Valid @RequestBody CarbonQuotaAssets data) {
        return ApiResult.ok(carbonQuotaAssetsService.save(data));
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新碳配额", notes = "更新碳配额")
    public ApiResult<Boolean> update(@Valid @RequestBody CarbonQuotaAssets data) {
        return ApiResult.ok(carbonQuotaAssetsService.updateById(data));
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除碳配额", notes = "删除碳配额")
    public ApiResult<Boolean> delete(@PathVariable("id") Long id) {
        return ApiResult.ok(carbonQuotaAssetsService.removeById(id));
    }
}
