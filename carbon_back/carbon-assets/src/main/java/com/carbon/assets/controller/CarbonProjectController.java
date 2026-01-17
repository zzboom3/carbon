package com.carbon.assets.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carbon.assets.entity.CarbonProject;
import com.carbon.assets.entity.CarbonProjectMonitoringData;
import com.carbon.assets.param.CarbonDataSubmissionQueryParam;
import com.carbon.assets.service.CarbonProjectService;
import com.carbon.assets.service.CarbonProjectMonitoringDataService;
import com.carbon.assets.param.CarbonProjectQueryParam;
import com.carbon.assets.vo.CarbonProjectListVo;
import com.carbon.assets.common.BaseController;
import com.carbon.domain.common.ApiResult;
import com.carbon.common.api.Paging;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.Valid;
import javax.annotation.Resource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * <p>
 * 碳减排项目 前端控制器
 * </p>
 *
 * @author Li Jun
 * @since 2021-07-31
 */
@Slf4j
@RestController
@RequestMapping("/carbonProject")
@Api(value = "碳减排项目模块", tags = {"碳减排项目模块"})
public class CarbonProjectController extends BaseController {

    @Resource
    private CarbonProjectService carbonProjectService;

    @Resource
    private CarbonProjectMonitoringDataService carbonProjectMonitoringDataService;

    @Value("${carbon.publicBaseUrl:}")
    private String publicBaseUrl;

    @PostMapping("/getDevelopPageList")
    @ApiOperation(value = "项目开发实施分页列表", notes = "项目开发实施分页列表")
    public ApiResult<Paging<CarbonProjectListVo>> getDevelopPageList(@Valid @RequestBody(required = false) CarbonProjectQueryParam param) {
        CarbonProjectQueryParam query = param == null ? new CarbonProjectQueryParam() : param;
        Page<CarbonProject> page = carbonProjectService.page(
                new Page<>(query.getCurrent(), query.getSize()),
                Wrappers.lambdaQuery(CarbonProject.class)
                        .like(StrUtil.isNotBlank(query.getName()), CarbonProject::getProjectName, query.getName())
                        .orderByDesc(CarbonProject::getId)
        );
        Page<CarbonProjectListVo> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(page.getRecords().stream().map(e -> {
            CarbonProjectListVo vo = new CarbonProjectListVo();
            BeanUtil.copyProperties(e, vo);
            return vo;
        }).collect(Collectors.toList()));
        return ApiResult.ok(new Paging<>(voPage));
    }

    @PostMapping("/getPageList")
    @ApiOperation(value = "项目分页列表", notes = "项目分页列表")
    public ApiResult<Paging<CarbonProjectListVo>> getPageList(@Valid @RequestBody(required = false) CarbonProjectQueryParam param) {
        return getDevelopPageList(param);
    }

    @PostMapping("/getNoWaitExaminePageList")
    @ApiOperation(value = "非待审核项目分页列表", notes = "非待审核项目分页列表")
    public ApiResult<Paging<CarbonProjectListVo>> getNoWaitExaminePageList(@Valid @RequestBody(required = false) CarbonProjectQueryParam param) {
        return getDevelopPageList(param);
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增项目", notes = "新增项目")
    public ApiResult<Boolean> add(@Valid @RequestBody CarbonProject data) {
        return ApiResult.ok(carbonProjectService.save(data));
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新项目", notes = "更新项目")
    public ApiResult<Boolean> update(@Valid @RequestBody CarbonProject data) {
        return ApiResult.ok(carbonProjectService.updateById(data));
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除项目", notes = "删除项目")
    public ApiResult<Boolean> delete(@PathVariable("id") Long id) {
        return ApiResult.ok(carbonProjectService.removeById(id));
    }

    @GetMapping("/info/{id}")
    @ApiOperation(value = "项目详情", notes = "项目详情")
    public ApiResult<CarbonProject> info(@PathVariable("id") Long id) {
        return ApiResult.ok(carbonProjectService.getById(id));
    }

    @PostMapping("/uploadOwnerData")
    @ApiOperation(value = "上传业主资料", notes = "上传业主资料")
    public Map<String, Object> uploadOwnerData(@RequestParam("file") MultipartFile file) throws Exception {
        String original = file.getOriginalFilename();
        String fileName = (original == null || original.trim().isEmpty()) ? "file" : original;
        String dateDir = LocalDate.now().toString();
        Path uploadDir = Paths.get(System.getProperty("user.dir"), "uploads", "assets", dateDir);
        Files.createDirectories(uploadDir);
        String safeName = java.util.UUID.randomUUID().toString().replace("-", "") + "_" + fileName;
        Path target = uploadDir.resolve(safeName);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        Map<String, Object> res = new HashMap<>();
        res.put("code", 200);
        res.put("msg", buildPublicUrl("/assets/upload/assets/" + dateDir + "/" + safeName));
        return res;
    }

    @PostMapping("/dataSubmissionPageList")
    @ApiOperation(value = "碳数据报送分页列表", notes = "碳数据报送分页列表")
    public ApiResult<Paging<Map<String, Object>>> dataSubmissionPageList(@Valid @RequestBody(required = false) CarbonDataSubmissionQueryParam param) {
        CarbonDataSubmissionQueryParam query = param == null ? new CarbonDataSubmissionQueryParam() : param;
        Long projectId = toLongOrNull(query.getProjectId());
        Long creatorId = toLongOrNull(query.getCreatorId());
        Page<CarbonProjectMonitoringData> page = carbonProjectMonitoringDataService.page(
                new Page<>(query.getCurrent(), query.getSize()),
                Wrappers.lambdaQuery(CarbonProjectMonitoringData.class)
                        .eq(projectId != null, CarbonProjectMonitoringData::getCarbonProjectId, projectId)
                        .eq(creatorId != null, CarbonProjectMonitoringData::getCreatorId, creatorId)
                        .like(StrUtil.isNotBlank(query.getFactorCode()), CarbonProjectMonitoringData::getParameter, query.getFactorCode())
                        .like(StrUtil.isNotBlank(query.getSourceFrom()), CarbonProjectMonitoringData::getSource, query.getSourceFrom())
                        .orderByDesc(CarbonProjectMonitoringData::getId)
        );

        Page<Map<String, Object>> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(page.getRecords().stream()
                .map(e -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", e.getId());
                    map.put("carbonProjectId", e.getCarbonProjectId());
                    map.put("status", e.getStatus());
                    map.put("parameter", e.getParameter());
                    map.put("unit", e.getUnit());
                    map.put("source", e.getSource());
                    map.put("dateValue", e.getDateValue());
                    map.put("measuringMethod", e.getMeasuringMethod());
                    map.put("frequency", e.getFrequency());
                    map.put("program", e.getProgram());
                    map.put("purpose", e.getPurpose());
                    map.put("appraise", e.getAppraise());
                    map.put("creatorId", e.getCreatorId());
                    map.put("createdTime", e.getCreatedTime());
                    map.put("updatedId", e.getUpdatedId());
                    map.put("updatedTime", e.getUpdatedTime());
                    return map;
                })
                .collect(Collectors.toList()));
        return ApiResult.ok(new Paging<>(voPage));
    }

    @GetMapping("/dataSubmissionPage/{id}")
    @ApiOperation(value = "获取项目碳数据报送列表", notes = "获取项目碳数据报送列表")
    public ApiResult<List<Map<String, Object>>> dataSubmissionPage(@PathVariable("id") Long id) {
        List<CarbonProjectMonitoringData> list = carbonProjectMonitoringDataService.list(
                Wrappers.lambdaQuery(CarbonProjectMonitoringData.class)
                        .eq(CarbonProjectMonitoringData::getCarbonProjectId, id)
                        .orderByDesc(CarbonProjectMonitoringData::getId)
        );
        List<Map<String, Object>> records = list.stream()
                .filter(Objects::nonNull)
                .map(e -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", e.getId());
                    map.put("carbonProjectId", e.getCarbonProjectId());
                    map.put("status", e.getStatus());
                    map.put("parameter", e.getParameter());
                    map.put("unit", e.getUnit());
                    map.put("source", e.getSource());
                    map.put("dateValue", e.getDateValue());
                    map.put("measuringMethod", e.getMeasuringMethod());
                    map.put("frequency", e.getFrequency());
                    map.put("program", e.getProgram());
                    map.put("purpose", e.getPurpose());
                    map.put("appraise", e.getAppraise());
                    map.put("creatorId", e.getCreatorId());
                    map.put("createdTime", e.getCreatedTime());
                    map.put("updatedId", e.getUpdatedId());
                    map.put("updatedTime", e.getUpdatedTime());
                    return map;
                })
                .collect(Collectors.toList());
        return ApiResult.ok(records);
    }

    private Long toLongOrNull(String v) {
        if (StrUtil.isBlank(v)) return null;
        try {
            return Long.parseLong(v);
        } catch (Exception e) {
            return null;
        }
    }

    private String buildPublicUrl(String relativePath) {
        if (StrUtil.isBlank(publicBaseUrl)) {
            return relativePath;
        }
        String base = StrUtil.trim(publicBaseUrl);
        while (base.endsWith("/")) {
            base = base.substring(0, base.length() - 1);
        }
        String rel = StrUtil.blankToDefault(relativePath, "");
        if (!rel.startsWith("/")) {
            rel = "/" + rel;
        }
        if (base.endsWith("/assets") && rel.startsWith("/assets/")) {
            rel = rel.substring("/assets".length());
        }
        return base + rel;
    }
}
