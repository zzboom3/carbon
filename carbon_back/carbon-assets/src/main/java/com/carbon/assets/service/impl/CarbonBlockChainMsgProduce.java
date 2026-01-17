package com.carbon.assets.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.carbon.assets.entity.CarbonAssets;
import com.carbon.assets.entity.CarbonAssetsBusiness;
import com.carbon.assets.vo.block.CarbonBlockExchangeInfo;
import com.carbon.common.exception.CommonBizException;
import com.carbon.domain.block.param.*;
import com.carbon.domain.block.vo.BlockData;
import com.carbon.domain.block.vo.BlockResponse;
import com.carbon.domain.block.vo.CarbonAssetsToken;
import com.carbon.domain.block.vo.CarbonAssetsTokenInfo;
import com.carbon.domain.chainmaker.api.ChainMakerServiceApi;
import com.carbon.domain.chainmaker.vo.ChainMakerRtVO;
import com.carbon.domain.chainmaker.vo.TransactionRtVO;
import com.carbon.domain.common.ApiResult;
import com.carbon.domain.common.constant.RocketDelayLevelConstant;
import com.carbon.domain.common.constant.RocketMqName;
import com.carbon.domain.common.constant.RocketMqTagName;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 *  <p>
 *   区块链服务 异步发送生产者  服务实现类
 *  </p>
 *
 *  @author Li Jun
 *  @since 2021-09-24
 */
@Service
@Slf4j
public class CarbonBlockChainMsgProduce {
    @Autowired
    private ChainMakerServiceApi chainMakerServiceApi;
    @Autowired
    private CarbonAssetsServiceImpl carbonAssetsService;
    @Autowired
    private CarbonAssetsBusinessServiceImpl carbonAssetsBusinessService;


    /**
     * 获取交易详情
     * @param txId 交易ID
     * @return CarbonBlockExchangeInfo
     */
    public CarbonBlockExchangeInfo getExchangeInfo(String txId) {
        ApiResult<TransactionRtVO> result = chainMakerServiceApi.queryTxByTxId(txId);
        if (result == null || result.getCode() != 200 || result.getData() == null) {
            throw new CommonBizException("区块链服务不可用");
        }
        CarbonBlockExchangeInfo info = new CarbonBlockExchangeInfo();
        info.setTxInfo(result.getData().getTxInfo());
        info.setBlockHash(result.getData().getBlockHash());
        info.setTimestamp(DateUtil.format(new Date(result.getData().getTimestamp()), "yyyy-MM-dd HH:mm:ss"));
        info.setTxInfo(result.getData().getTxInfo());
        return info;

    }

    /**
     * 碳减排资产上链 同步操作
     * @param param 参数
     */
    public String syncAddCarbonOffset(CarbonOffsetAddParam param) {
        try {
            Map<String, Object> raw = BeanUtil.beanToMap(param, false, true);
            Map<String, String> map = new HashMap<>();
            raw.forEach((k, v) -> {
                if (v != null) {
                    map.put(k, String.valueOf(v));
                }
            });
            ApiResult<ChainMakerRtVO> blockResponse = chainMakerServiceApi.AddCarbonOffset(map);
            if (blockResponse == null || blockResponse.getCode() != 200 || blockResponse.getData() == null) {
                throw new CommonBizException("区块链上链失败");
            }
            return blockResponse.getData().getTxId();
        } catch (Exception e) {
            log.error("碳减排资产上链失败", e);
            if (e instanceof CommonBizException) {
                throw (CommonBizException) e;
            }
            throw new CommonBizException("区块链上链失败");
        }
    }


    /**
     * 中和资产认证 中和资产链上核证
     * @param assetsId 参数
     */
    public void assetsCertified(String assetsId) {
        CarbonAssets carbonAssets = carbonAssetsService.getById(assetsId);
        //中和资产链上核证
        CarbonCertifiedParam param = new CarbonCertifiedParam();
        param.setUserId("admin");
        param.setOrgName("xihu_taxi");
        param.setEmissionsRecordsToAudit(carbonAssets.getCarbonSourceDataId());
        param.setAutomaticRetireDate(DateUtil.offsetHour(DateUtil.date(),1).toString());
        param.setPublishId(String.valueOf(carbonAssets.getPublishId()) );
        param.setReceivingId(String.valueOf(carbonAssets.getReceivingId()));
        param.setAssetType("0");
        param.setTimeScope(carbonAssets.getTimeScope());
        param.setMetaData(carbonAssets.getMetaData());
        param.setMainList(carbonAssets.getMainList());
        param.setSign(true);
        ApiResult<ChainMakerRtVO> rt = chainMakerServiceApi.addAssetsCertified(param);
        if (rt == null || rt.getCode() != 200 || rt.getData() == null || rt.getData().getTokenId() == null) {
            throw new CommonBizException("区块链核证失败");
        }
        String certifiedTokenId = rt.getData().getTokenId();
        //更新状态
        carbonAssets.setCertifiedTxId(certifiedTokenId);
        carbonAssets.setStatus(2);
        carbonAssetsService.updateById(carbonAssets);
    }
    /**
     * 中和资产交易
     * @param param 参数
     */
    public void assetsBusiness(CarbonAssetsBusiness param) {
        // 交易id
        ApiResult<ChainMakerRtVO> rt = chainMakerServiceApi
                .addAssetsBusiness(CarbonAssetsBusiness.buildCarbonBusinessParam(param));
        if (rt == null || rt.getCode() != 200 || rt.getData() == null || rt.getData().getTxId() == null) {
            throw new CommonBizException("区块链交易失败");
        }
        String txId = rt.getData().getTxId();
        // 操作
        param.setTxId(txId);
        carbonAssetsBusinessService.save(param);
    }


    /** ============================================================================  */
    /**
     * 注册机构管理员账户
     * @param param 参数
     */
    public void registerAdmin(AccountRegisterParam param) {
        Map<String, Object> raw = BeanUtil.beanToMap(param, false, true);
        Map<String, String> map = new HashMap<>();
        raw.forEach((k, v) -> {
            if (v != null) {
                map.put(k, String.valueOf(v));
            }
        });
        chainMakerServiceApi.registerAdmin(map);
    }

    /**
     * 注册机构用户账户
     * @param param 参数
     */
    public void registerUser(AccountRegisterParam param) {
        Map<String, Object> raw = BeanUtil.beanToMap(param, false, true);
        Map<String, String> map = new HashMap<>();
        raw.forEach((k, v) -> {
            if (v != null) {
                map.put(k, String.valueOf(v));
            }
        });
        chainMakerServiceApi.registerExchangeAccount(map);
    }

    /**
     * 碳配额资产上链
     * @param param 参数
     */
    public void addCarbonQuotas(CarbonQuotasAddParam param) {
        throw new CommonBizException("碳配额上链接口未接入");
    }
    /**
     * 碳减排资产上链
     * @param param 参数
     */
    public void addCarbonOffset(CarbonOffsetAddParam param) {
        syncAddCarbonOffset(param);
    }


    /**
     * 查看机构中和资产token列表
     * @param param 参数
     */
    public CarbonAssetsToken queryAssetsTokenList(CarbonAssetsTokenParam param) {
        throw new CommonBizException("资产 token 查询接口未接入");
    }
    /**
     * 查看机构中和资产token详情
     * @param param 参数
     */
    public CarbonAssetsTokenInfo queryAssetsTokenInfo(CarbonAssetsTokenParam param) {
        throw new CommonBizException("资产 token 查询接口未接入");
    }
}
