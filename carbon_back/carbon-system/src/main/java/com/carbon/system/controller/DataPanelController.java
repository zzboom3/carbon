package com.carbon.system.controller;

import  com.carbon.system.service.FeishuFiletokenService;
import com.carbon.system.vo.EnterpriseGreennessVo;
import com.carbon.system.service.DataPanelService;
import com.carbon.system.vo.StatHomeDataVo;
import com.carbon.system.vo.StatCarbonQuotationProjectVo;
import com.carbon.system.vo.StatCarbonQuotationVo;
import com.carbon.system.vo.StatAccountVo;
import com.carbon.system.vo.StatCarbonIncomeVo;
import com.carbon.system.vo.StatCarbonMonthVo;
import com.carbon.system.vo.StatCarbonProjectVo;
import com.carbon.system.common.BaseController;
import com.carbon.domain.common.ApiResult;
import com.carbon.common.utils.HttpContextUtils;
import com.carbon.system.entity.SysAccount;
import com.carbon.system.entity.SysTenant;
import com.carbon.system.mapper.SysAccountMapper;
import com.carbon.system.mapper.SysTenantMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.math.BigDecimal;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 数据面板 前端控制器
 * </p>
 *
 * @author Li Jun
 * @since 2021-07-20
 */
@Slf4j
@RestController
@RequestMapping("/dataPanel")
@Api(value = "数据面板模块", description = "数据面板模块", tags = {"数据面板模块"})
public class DataPanelController extends BaseController {
    @Resource
    private SysAccountMapper sysAccountMapper;

    @Resource
    private SysTenantMapper sysTenantMapper;

    private static StatCarbonIncomeVo zeroIncome(Date now) {
        StatCarbonIncomeVo vo = new StatCarbonIncomeVo();
        vo.setStatDate(now);
        vo.setMonthIncome(BigDecimal.ZERO);
        vo.setTotalIncome(BigDecimal.ZERO);
        vo.setMonthOnMonthRatio(BigDecimal.ZERO);
        vo.setYearOnYearRatio(BigDecimal.ZERO);
        return vo;
    }

    private static StatCarbonMonthVo zeroMonth(Date now) {
        StatCarbonMonthVo vo = new StatCarbonMonthVo();
        vo.setStatDate(now);
        vo.setCarbonSupply(BigDecimal.ZERO);
        vo.setCarbonCredit(BigDecimal.ZERO);
        vo.setCarbonQuota(BigDecimal.ZERO);
        vo.setGreenScore(BigDecimal.ZERO);
        vo.setCarbonSupplyTotal(BigDecimal.ZERO);
        vo.setCarbonValuation(BigDecimal.ZERO);
        return vo;
    }

    private static StatCarbonProjectVo zeroProject() {
        StatCarbonProjectVo vo = new StatCarbonProjectVo();
        vo.setReductionTotal(BigDecimal.ZERO);
        vo.setApprovedCount(0);
        vo.setFilingCount(0);
        vo.setSingCount(0);
        vo.setProjectList(new ArrayList<>());
        return vo;
    }

    private StatAccountVo buildAccountVo(Date now) {
        StatAccountVo vo = new StatAccountVo();
        Long accountId = HttpContextUtils.getAccountId();
        if (accountId == null) {
            vo.setExpired(false);
            vo.setAccountValidity(now);
            return vo;
        }
        SysAccount account = sysAccountMapper.selectById(accountId);
        if (account == null) {
            vo.setExpired(false);
            vo.setAccountValidity(now);
            return vo;
        }
        vo.setAccountName(account.getAccountName());
        vo.setAvatar(account.getAvatar());
        vo.setProductVersion(account.getProductVersion());
        vo.setAccountType(account.getAccountType());
        vo.setRoleNames(new ArrayList<>());
        Date validity = account.getValidityPeriod();
        if (account.getTenantId() != null) {
            SysTenant tenant = sysTenantMapper.selectById(account.getTenantId());
            if (tenant != null) {
                vo.setEnterpriseName(tenant.getTenantName());
                if (validity == null) {
                    validity = tenant.getValidityTime();
                }
            }
        }
        if (validity == null) {
            validity = now;
        }
        vo.setAccountValidity(validity);
        vo.setExpired(validity.before(now));
        vo.setCarbonNeutralRatio(BigDecimal.ZERO);
        vo.setEsgScore(BigDecimal.ZERO);
        return vo;
    }

    @GetMapping("/home")
    @ApiOperation(value = "首页数据面板", notes = "首页数据面板")
    public ApiResult<StatHomeDataVo> home() {
        StatHomeDataVo vo = new StatHomeDataVo();
        Date now = new Date();

        vo.setAssetsIncome(zeroIncome(now));
        vo.setFundIncome(zeroIncome(now));
        vo.setAccountVo(buildAccountVo(now));
        vo.setCarbonCredit(BigDecimal.ZERO);
        vo.setCarbonQuota(BigDecimal.ZERO);
        vo.setGreenScore(BigDecimal.ZERO);
        vo.setMonthSupply(zeroMonth(now));
        vo.setMonthDevelopment(zeroMonth(now));
        vo.setMonthSales(zeroMonth(now));
        vo.setProjectStat(zeroProject());

        StatCarbonQuotationVo quotation = new StatCarbonQuotationVo();
        quotation.setType("VCS");
        quotation.setStatDate(now);
        quotation.setCcerCount(100L);
        quotation.setStockCount(60L);
        quotation.setWrittenOffCount(40L);
        quotation.setCcerProjectCount(10L);
        quotation.setApprovedCount(3L);
        quotation.setFilingCount(3L);
        quotation.setSingCount(4L);

        StatCarbonQuotationProjectVo project = new StatCarbonQuotationProjectVo();
        project.setType("默认");
        project.setSingCount(new BigDecimal("4"));
        project.setStockCount(new BigDecimal("6"));
        project.setCount(new BigDecimal("10"));

        List<StatCarbonQuotationProjectVo> projects = new ArrayList<>();
        projects.add(project);
        quotation.setProjects(projects);

        List<StatCarbonQuotationVo> quotationList = new ArrayList<>();
        quotationList.add(quotation);
        vo.setQuotation(quotationList);
        return ApiResult.ok(vo);
    }

    @GetMapping("/getGreenness")
    @ApiOperation(value = "首页-企业绿度", notes = "首页-企业绿度")
    public ApiResult<EnterpriseGreennessVo> getGreenness() {
        EnterpriseGreennessVo vo = new EnterpriseGreennessVo();
        vo.setCarbonEmissionRate(0.33);
        vo.setEnergyEfficiencyRate(0.34);
        vo.setEnvironmentRate(0.33);
        return ApiResult.ok(vo);
    }

    @GetMapping("/getCarbonAssets")
    @ApiOperation(value = "首页-运营数据", notes = "首页-运营数据")
    public ApiResult<Map<String, Object>> getCarbonAssets() {
        Map<String, Object> data = new HashMap<>();
        data.put("carbonReduce", 0);
        data.put("carbonCertificationCount", 0);
        data.put("carbonMethodologyCount", 0);
        data.put("carbonExchangeCount", 0);
        data.put("carbonAssetsList", new ArrayList<>());
        return ApiResult.ok(data);
    }
}
