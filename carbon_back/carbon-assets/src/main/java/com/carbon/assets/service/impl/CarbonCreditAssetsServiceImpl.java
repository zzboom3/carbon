package com.carbon.assets.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.carbon.assets.common.enums.AssetsStatus;
import com.carbon.assets.common.enums.AssetsTradeStatus;
import com.carbon.assets.entity.CarbonCreditAssets;
import com.carbon.assets.entity.CarbonMetaregistry;
import com.carbon.assets.entity.CarbonMetaregistryProject;
import com.carbon.assets.entity.CarbonProject;
import com.carbon.assets.mapper.*;
import com.carbon.assets.param.CarbonCreditAssetsAddParam;
import com.carbon.assets.param.CarbonProjectAddParam;
import com.carbon.assets.service.CarbonCreditAssetsService;
import com.carbon.assets.param.CarbonCreditAssetsQueryParam;
import com.carbon.assets.service.CarbonProjectService;
import com.carbon.assets.vo.CarbonAssetsTotalVo;
import com.carbon.assets.vo.CarbonCreditAssetsQueryVo;
import com.carbon.assets.vo.CarbonMetaregistryQueryVo;
import com.carbon.common.service.BaseServiceImpl;
import com.carbon.common.api.Paging;
import com.carbon.domain.auth.vo.SecurityData;
import com.carbon.domain.chainmaker.api.ChainMakerServiceApi;
import com.carbon.domain.chainmaker.param.CarbonCreditAssetsParam;
import com.carbon.domain.common.constant.RocketDelayLevelConstant;
import com.carbon.domain.common.constant.RocketMqName;
import com.carbon.domain.mq.entity.AssetUploadApproval;
import com.carbon.domain.system.api.SystemServiceApi;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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

}
