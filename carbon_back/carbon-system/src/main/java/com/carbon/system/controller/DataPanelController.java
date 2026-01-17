package com.carbon.system.controller;

import com.carbon.system.service.FeishuFiletokenService;
import com.carbon.system.vo.EnterpriseGreennessVo;
import com.carbon.system.service.DataPanelService;
import com.carbon.system.vo.StatHomeDataVo;
import com.carbon.system.vo.StatCarbonQuotationProjectVo;
import com.carbon.system.vo.StatCarbonQuotationVo;
import com.carbon.system.vo.StatAccountVo;
import com.carbon.system.vo.StatCarbonIncomeVo;
import com.carbon.system.vo.StatCarbonMonthVo;
import com.carbon.system.vo.StatCarbonProjectVo;
import com.carbon.system.vo.StatCarbonProject;
import com.carbon.system.common.BaseController;
import com.carbon.domain.common.ApiResult;
import com.carbon.common.utils.HttpContextUtils;
import com.carbon.system.entity.CarbonProject;
import com.carbon.system.entity.SysAccount;
import com.carbon.system.entity.SysTenant;
import com.carbon.system.mapper.CarbonProjectMapper;
import com.carbon.system.mapper.SysAccountMapper;
import com.carbon.system.mapper.SysTenantMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;

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

    @Resource
    private CarbonProjectMapper carbonProjectMapper;

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

    private static boolean notBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private static int toInt(Long value) {
        if (value == null) {
            return 0;
        }
        if (value > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return value.intValue();
    }

    private StatCarbonProjectVo buildProjectStat(Long tenantId) {
        StatCarbonProjectVo vo = zeroProject();
        QueryWrapper<CarbonProject> approvedWrapper = new QueryWrapper<>();
        if (tenantId != null) {
            approvedWrapper.eq("tenant_id", tenantId);
        }
        approvedWrapper.isNotNull("approval_date");
        Integer approvedCount = carbonProjectMapper.selectCount(approvedWrapper);

        QueryWrapper<CarbonProject> filingWrapper = new QueryWrapper<>();
        if (tenantId != null) {
            filingWrapper.eq("tenant_id", tenantId);
        }
        filingWrapper.isNotNull("record_filing_date");
        Integer filingCount = carbonProjectMapper.selectCount(filingWrapper);

        QueryWrapper<CarbonProject> singWrapper = new QueryWrapper<>();
        if (tenantId != null) {
            singWrapper.eq("tenant_id", tenantId);
        }
        singWrapper.isNotNull("issuing_date");
        Integer singCount = carbonProjectMapper.selectCount(singWrapper);

        vo.setApprovedCount(approvedCount == null ? 0 : approvedCount);
        vo.setFilingCount(filingCount == null ? 0 : filingCount);
        vo.setSingCount(singCount == null ? 0 : singCount);
        vo.setReductionTotal(BigDecimal.ZERO);

        Page<CarbonProject> page = new Page<>(1, 5);
        QueryWrapper<CarbonProject> listWrapper = new QueryWrapper<>();
        if (tenantId != null) {
            listWrapper.eq("tenant_id", tenantId);
        }
        listWrapper.orderByDesc("id");
        IPage<CarbonProject> pageResult = carbonProjectMapper.selectPage(page, listWrapper);
        List<StatCarbonProject> list = new ArrayList<>();
        for (CarbonProject p : pageResult.getRecords()) {
            StatCarbonProject item = new StatCarbonProject();
            item.setProjectName(p.getProjectName());
            item.setReduction("0");
            item.setCarbonValuation(BigDecimal.ZERO);
            item.setDevelopmentState(p.getProjectStatus());
            item.setTradeState("");
            list.add(item);
        }
        vo.setProjectList(list);
        return vo;
    }

    private List<StatCarbonQuotationVo> buildQuotation(Date now, Long tenantId) {
        QueryWrapper<CarbonProject> totalWrapper = new QueryWrapper<>();
        if (tenantId != null) {
            totalWrapper.eq("tenant_id", tenantId);
        }
        Integer totalProjects = carbonProjectMapper.selectCount(totalWrapper);

        QueryWrapper<CarbonProject> approvedWrapper = new QueryWrapper<>();
        if (tenantId != null) {
            approvedWrapper.eq("tenant_id", tenantId);
        }
        approvedWrapper.isNotNull("approval_date");
        Integer approvedCount = carbonProjectMapper.selectCount(approvedWrapper);

        QueryWrapper<CarbonProject> filingWrapper = new QueryWrapper<>();
        if (tenantId != null) {
            filingWrapper.eq("tenant_id", tenantId);
        }
        filingWrapper.isNotNull("record_filing_date");
        Integer filingCount = carbonProjectMapper.selectCount(filingWrapper);

        QueryWrapper<CarbonProject> singWrapper = new QueryWrapper<>();
        if (tenantId != null) {
            singWrapper.eq("tenant_id", tenantId);
        }
        singWrapper.isNotNull("issuing_date");
        Integer singCount = carbonProjectMapper.selectCount(singWrapper);

        StatCarbonQuotationVo quotation = new StatCarbonQuotationVo();
        quotation.setType("VCS");
        quotation.setStatDate(now);
        quotation.setCcerProjectCount(totalProjects == null ? 0L : totalProjects.longValue());
        quotation.setApprovedCount(approvedCount == null ? 0L : approvedCount.longValue());
        quotation.setFilingCount(filingCount == null ? 0L : filingCount.longValue());
        quotation.setSingCount(singCount == null ? 0L : singCount.longValue());
        quotation.setCcerCount(singCount == null ? 0L : singCount.longValue());
        quotation.setStockCount(filingCount == null ? 0L : filingCount.longValue());
        quotation.setWrittenOffCount(0L);

        StatCarbonQuotationProjectVo project = new StatCarbonQuotationProjectVo();
        project.setType("项目");
        project.setCount(BigDecimal.valueOf(totalProjects == null ? 0L : totalProjects.longValue()));
        project.setStockCount(BigDecimal.valueOf(filingCount == null ? 0L : filingCount.longValue()));
        project.setSingCount(BigDecimal.valueOf(singCount == null ? 0L : singCount.longValue()));

        List<StatCarbonQuotationProjectVo> projects = new ArrayList<>();
        projects.add(project);
        quotation.setProjects(projects);

        List<StatCarbonQuotationVo> list = new ArrayList<>();
        list.add(quotation);
        return list;
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
        Long tenantId = HttpContextUtils.getTenantId();

        vo.setAssetsIncome(zeroIncome(now));
        vo.setFundIncome(zeroIncome(now));
        vo.setAccountVo(buildAccountVo(now));
        vo.setCarbonCredit(BigDecimal.ZERO);
        vo.setCarbonQuota(BigDecimal.ZERO);
        vo.setGreenScore(BigDecimal.ZERO);
        vo.setMonthSupply(zeroMonth(now));
        vo.setMonthDevelopment(zeroMonth(now));
        vo.setMonthSales(zeroMonth(now));
        vo.setProjectStat(buildProjectStat(tenantId));
        vo.setQuotation(buildQuotation(now, tenantId));
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
