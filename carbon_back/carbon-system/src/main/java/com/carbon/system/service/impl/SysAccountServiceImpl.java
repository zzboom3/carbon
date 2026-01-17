package com.carbon.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carbon.common.api.Paging;
import com.carbon.common.enums.ExpCodeEnum;
import com.carbon.common.exception.CommonBizException;
import com.carbon.common.redis.RedisService;
import com.carbon.common.service.BaseServiceImpl;
import com.carbon.domain.common.constant.RedisKeyName;
import com.carbon.domain.common.constant.RocketDelayLevelConstant;
import com.carbon.domain.common.constant.RocketMqName;
import com.carbon.domain.system.param.ChangePasswordParam;
import com.carbon.domain.system.param.SysAccountParam;
import com.carbon.system.entity.SysAccount;
import com.carbon.system.param.*;
import com.carbon.system.service.SysAccountRoleService;
import com.carbon.system.service.common.MailService;
import com.carbon.system.vo.SysAccountQueryVo;
import com.carbon.system.mapper.SysAccountMapper;
import com.carbon.system.service.SysAccountService;
import com.carbon.system.service.SysRoleService;
import com.carbon.system.vo.SysAccountRoleVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * <p>
 * 帐号  服务实现类
 * </p>
 *
 * @author Li Jun
 * @since 2021-06-11
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SysAccountServiceImpl extends BaseServiceImpl<SysAccountMapper, SysAccount> implements SysAccountService {

    @Autowired
    private SysAccountRoleService sysAccountRoleService;

    @Override
    public boolean addSysAccount(SysAccountParam param) {
        SysAccount existedByName = this.lambdaQuery().eq(SysAccount::getAccountName, param.getAccountName()).one();
        if (existedByName != null) {
            throw new CommonBizException(ExpCodeEnum.SYS_ACCOUNT_NAME_ALREADY_EXISTS);
        }
        SysAccount existedByPhone = this.lambdaQuery().eq(SysAccount::getPhone, param.getPhone()).one();
        if (existedByPhone != null) {
            throw new CommonBizException(ExpCodeEnum.SYS_ACCOUNT_PHONE_ALREADY_EXISTS);
        }
        SysAccount sysAccount = new SysAccount();
        BeanUtil.copyProperties(param, sysAccount);
        sysAccount.setPassword(DigestUtils.md5Hex(param.getPassword()));
        boolean save = this.save(sysAccount);
        if (save && CollUtil.isNotEmpty(param.getRoleIds())){
            List<com.carbon.system.entity.SysAccountRole> roleList = new java.util.ArrayList<>();
            param.getRoleIds().forEach(roleId -> {
                com.carbon.system.entity.SysAccountRole role = new com.carbon.system.entity.SysAccountRole();
                role.setAccountId(sysAccount.getId());
                role.setRoleId(roleId);
                roleList.add(role);
            });
            sysAccountRoleService.saveBatch(roleList);
        }
        return save;
    }

    @Override
    public boolean updatePassword(ChangePasswordParam param) {
        SysAccount sysAccount = this.getById(param.getId());
        if (sysAccount == null) {
            throw new CommonBizException(ExpCodeEnum.SYS_ACCOUNT_NOT_EXISTS);
        }
        sysAccount.setPassword(DigestUtils.md5Hex(param.getNewPassword()));
        return this.updateById(sysAccount);
    }

    @Override
    public List<com.carbon.domain.system.vo.SysAccountModelVo> getAccountList() {
        return BeanUtil.copyToList(this.list(), com.carbon.domain.system.vo.SysAccountModelVo.class);
    }
}
