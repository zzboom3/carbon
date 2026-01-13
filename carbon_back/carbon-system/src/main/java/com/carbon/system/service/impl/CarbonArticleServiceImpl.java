package com.carbon.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carbon.common.entity.AddResponse;
import com.carbon.common.exception.CommonBizException;
import com.carbon.common.feishu.FeiShuAPI;
import com.carbon.common.redis.RedisService;
import com.carbon.domain.common.constant.RedisKeyName;
import com.carbon.system.config.OkHttpCli;
import com.carbon.system.entity.CarbonArticle;
import com.carbon.system.entity.CarbonArticleComment;
import com.carbon.system.mapper.CarbonArticleCommentMapper;
import com.carbon.system.mapper.CarbonArticleMapper;
import com.carbon.system.param.CarbonArticleStatuParam;
import com.carbon.system.service.CarbonArticleService;
import com.carbon.system.param.CarbonArticleQueryParam;
import com.carbon.system.service.FeishuFiletokenService;
import com.carbon.system.util.CommonUtil;
import com.carbon.system.vo.CarbonArticleAddVo;
import com.carbon.system.vo.CarbonArticleQueryVo;
import com.carbon.common.service.BaseServiceImpl;
import com.carbon.common.api.Paging;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import okhttp3.*;
import org.apache.http.client.methods.HttpHead;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * <p>
 * 碳文章 服务实现类
 * </p>
 *
 * @author Li Jun
 * @since 2021-08-01
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class CarbonArticleServiceImpl extends BaseServiceImpl<CarbonArticleMapper, CarbonArticle> implements CarbonArticleService {

}
