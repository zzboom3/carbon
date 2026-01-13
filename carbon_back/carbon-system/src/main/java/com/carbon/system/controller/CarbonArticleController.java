package com.carbon.system.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carbon.system.entity.CarbonArticleComment;
import com.carbon.system.mapper.CarbonArticleCommentMapper;
import com.carbon.system.param.CarbonArticleParam;
import com.carbon.system.common.BaseController;
import com.carbon.system.param.CarbonArticleStatuParam;
import com.carbon.system.service.CarbonArticleService;
import com.carbon.system.service.CarbonH5ArticleService;
import com.carbon.system.param.CarbonArticleQueryParam;
import com.carbon.system.service.FeishuFiletokenService;
import com.carbon.system.vo.CarbonArticleAddVo;
import com.carbon.system.vo.CarbonArticleQueryVo;
import com.carbon.system.entity.CarbonArticle;
import com.carbon.domain.common.ApiResult;
import com.carbon.common.api.Paging;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;


/**
 * <p>
 * 碳文章 前端控制器
 * </p>
 *
 * @author Li Jun
 * @since 2021-08-01
 */
@Slf4j
@RestController
@RequestMapping("/carbonArticle")
@Api(value = "碳文章模块", tags = {"碳文章模块"})
public class CarbonArticleController extends BaseController {

    @Resource
    private CarbonH5ArticleService carbonH5ArticleService;

    @PostMapping("/getPageList")
    @ApiOperation(value = "分页查询资讯", notes = "分页查询资讯")
    public ApiResult<Paging<CarbonArticleQueryVo>> getPageList(@Valid @RequestBody(required = false) CarbonArticleQueryParam param) {
        CarbonArticleQueryParam query = param == null ? new CarbonArticleQueryParam() : param;
        return ApiResult.ok(carbonH5ArticleService.getCarbonArticlePageList(query));
    }
}
