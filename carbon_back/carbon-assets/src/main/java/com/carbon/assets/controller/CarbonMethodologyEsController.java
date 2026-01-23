package com.carbon.assets.controller;

import cn.hutool.json.JSONUtil;
import com.carbon.assets.common.BaseController;
import com.carbon.assets.entity.CarbonMethodology;
import com.carbon.assets.entity.CarbonMethodologyContent;
import com.carbon.assets.entity.EsCarbonMethodology;
import com.carbon.assets.param.CarbonMethodologyQueryParam;
import com.carbon.assets.repository.MethodologyRepository;
import com.carbon.assets.service.CarbonMethodologyService;
import com.carbon.assets.vo.CarbonMethodologyQueryVo;
import com.carbon.assets.vo.CarbonMethodologySelectVo;
import com.carbon.common.api.Paging;
import com.carbon.domain.common.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 碳减排方法学es搜索控制器
 * </p>
 *
 * @author cbd
 * @since 2021-07-31
 */
@Slf4j
@RestController
@RequestMapping("/EscarbonMethodology")
@Api(value = "碳减排方法学模块es搜索", tags = {"碳减排方法学ES模块"})
public class CarbonMethodologyEsController extends BaseController {

    @Autowired
    private MethodologyRepository methodologyRepository;

    @PostMapping("/getByKeyword")
    @ApiOperation(value = "方法学全文检索列表",notes = "方法学全文检索列表")
    public ApiResult<Map<String, Object>> findByKeyword(@Valid @RequestBody(required = false) CarbonMethodologyQueryParam carbonMethodologyQueryParam) {
        CarbonMethodologyQueryParam param = carbonMethodologyQueryParam == null ? new CarbonMethodologyQueryParam() : carbonMethodologyQueryParam;

        boolean hasKeyword = StringUtils.hasText(param.getSearchKey());
        boolean hasFilter = param.getCertificationCriteria() != null
                || StringUtils.hasText(param.getFieldCode())
                || StringUtils.hasText(param.getIndustryCode())
                || StringUtils.hasText(param.getStatusCode());

        try {
            Page<CarbonMethodologyContent> methodologies;
            if (hasKeyword) {
                String k = param.getSearchKey();
                methodologies = methodologyRepository.findByNameOrContentOrIndustryCodeNameOrDictCode(
                        k, k, k, k, PageRequest.of(0, 10000)
                );
            } else if (hasFilter) {
                methodologies = methodologyRepository.findAll(PageRequest.of(0, 10000));
            } else {
                methodologies = methodologyRepository.findAll(PageRequest.of(param.getCurrent() - 1, param.getSize()));
            }
            return ApiResult.ok(getSearchResMap(methodologies, param, hasFilter || hasKeyword));
        } catch (Exception e) {
            log.error("methodology es search failed, param={}", JSONUtil.toJsonStr(param), e);
            return ApiResult.ok(emptyResMap(param));
        }
    }

    @PostMapping("/getByKeyword2")
    @ApiOperation(value = "方法学全文检索列表",notes = "方法学全文检索列表")
    public ApiResult<Map<String, Object>> findByKeyword2(@Valid @RequestBody(required = false) CarbonMethodologyQueryParam carbonMethodologyQueryParam) {
        return findByKeyword(carbonMethodologyQueryParam);
    }

    private Map<String, Object> emptyResMap(CarbonMethodologyQueryParam param) {
        Map<String, Object> res = new HashMap<>();
        res.put("current", param.getCurrent());
        res.put("total", 0L);
        res.put("data", new ArrayList<>());
        return res;
    }

    private Map<String, Object> getSearchResMap(Page<CarbonMethodologyContent> methodologies, CarbonMethodologyQueryParam param, boolean needSlice) {
        String certificationCriteria = param.getCertificationCriteria();
        String fieldCode = param.getFieldCode();
        String industryCode = param.getIndustryCode();
        String statusCode = param.getStatusCode();

        List<CarbonMethodologyContent> filtered = new ArrayList<>();
        for (CarbonMethodologyContent item : methodologies.getContent()) {
            if (certificationCriteria != null || StringUtils.hasText(fieldCode) || StringUtils.hasText(industryCode) || StringUtils.hasText(statusCode)) {
                if (Match(certificationCriteria, fieldCode, industryCode, statusCode, item) != 1) {
                    continue;
                }
            }
            item.setContent("");
            filtered.add(item);
        }

        if (!needSlice) {
            Map<String, Object> res = new HashMap<>();
            res.put("current", param.getCurrent());
            res.put("total", methodologies.getTotalElements());
            res.put("data", filtered);
            return res;
        }

        int current = param.getCurrent() == null ? 1 : param.getCurrent();
        int size = param.getSize() == null ? 10 : param.getSize();
        int start = Math.max(0, (current - 1) * size);
        int end = Math.min(filtered.size(), start + size);
        List<CarbonMethodologyContent> pageItems = start >= filtered.size() ? new ArrayList<>() : filtered.subList(start, end);

        Map<String, Object> res = new HashMap<>();
        res.put("current", current);
        res.put("total", (long) filtered.size());
        res.put("data", pageItems);
        return res;
    }

    public int Match(String certificationCriteria,String fieldCode,String industryCode,String statusCode,CarbonMethodologyContent carbonMethodologyContent)
    {
        if(certificationCriteria!=null&&!carbonMethodologyContent.getCertificationCriteria().equals(certificationCriteria))
        {
            return 0;
        }
        if(fieldCode!=null&&!carbonMethodologyContent.getFieldCode().equals(fieldCode))
        {
            return 0;
        }
        if(industryCode!=null&&!carbonMethodologyContent.getIndustryCode().equals(industryCode))
        {
            return 0;

        }
        if(statusCode!=null&&!carbonMethodologyContent.getStatusCode().equals(statusCode))
        {
            return 0;
        }
        return 1;
    }
}
