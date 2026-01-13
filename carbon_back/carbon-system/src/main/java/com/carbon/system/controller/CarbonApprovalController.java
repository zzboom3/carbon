package com.carbon.system.controller;

import cn.hutool.core.bean.BeanUtil;
import com.carbon.common.api.Paging;
import com.carbon.common.enums.ApprovalCodeEnum;
import com.carbon.domain.common.ApiResult;
import com.carbon.domain.mq.entity.AddTradingAccountApproval;
import com.carbon.domain.mq.entity.AssetUploadApproval;
import com.carbon.domain.mq.entity.ProjectApproval;
import com.carbon.domain.mq.entity.QuotaApproval;
import com.carbon.system.common.BaseController;
import com.carbon.system.entity.CarbonArticle;
import com.carbon.system.param.CarbonArticleParam;
import com.carbon.system.param.CarbonArticleQueryParam;
import com.carbon.system.param.CarbonArticleStatuParam;
import com.carbon.system.service.CarbonApprovalService;
import com.carbon.system.service.CarbonArticleService;
import com.carbon.system.vo.CarbonArticleAddVo;
import com.carbon.system.vo.CarbonArticleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * <p>
 * 审批
 * </p>
 *
 * @author Li Jun
 * @since 2021-08-01
 */
@Slf4j
@RestController
@RequestMapping("/approval")
@Api(value = "审批模块", tags = {"审批模块"})
public class CarbonApprovalController extends BaseController {

}

