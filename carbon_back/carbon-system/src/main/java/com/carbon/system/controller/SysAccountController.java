package com.carbon.system.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carbon.common.api.Paging;
import com.carbon.common.redis.RedisService;
import com.carbon.domain.common.ApiResult;
import com.carbon.domain.common.constant.RedisKeyName;
import com.carbon.domain.system.param.ChangePasswordParam;
import com.carbon.domain.system.param.SysAccountParam;
import com.carbon.domain.system.vo.SysAccountModelVo;
import com.carbon.system.entity.SysAccount;
import com.carbon.system.param.*;
import com.carbon.system.vo.SysAccountQueryVo;
import com.carbon.system.vo.SysAccountRoleVo;
import com.carbon.system.common.BaseController;
import com.carbon.system.entity.SysAccountRole;
import com.carbon.system.entity.SysRole;
import com.carbon.system.service.SysAccountService;
import com.carbon.system.service.SysAccountRoleService;
import com.carbon.system.service.SysRoleService;
import com.carbon.system.service.common.MailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.ArrayList;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * <p>
 * 帐号  前端控制器
 * </p>
 *
 * @author Li Jun
 * @since 2021-06-11
 */
@Slf4j
@RestController
@RequestMapping("/sysAccount")
@Api(value = "账户 模块", tags = {"帐户 模块"})
public class SysAccountController extends BaseController {

    @Resource
    private SysAccountService sysAccountService;

    @Resource
    private SysAccountRoleService sysAccountRoleService;

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private RedisService redisService;

    @Resource
    private MailService mailService;

    @PostMapping("/add")
    @ApiOperation("添加账户")
    public ApiResult<Boolean> addSysAccount(@RequestBody @Valid SysAccountParam param) {
        return ApiResult.ok(sysAccountService.addSysAccount(param));
    }

    @PostMapping("/update/password")
    @ApiOperation("修改密码")
    public ApiResult<Boolean> updatePassword(@RequestBody @Valid ChangePasswordParam param) {
        return ApiResult.ok(sysAccountService.updatePassword(param));
    }

    @GetMapping("/list")
    @ApiOperation("获取账户列表")
    public ApiResult<List<SysAccountModelVo>> getAccountList() {
        return ApiResult.ok(sysAccountService.getAccountList());
    }

    @GetMapping("/info/{id}")
    @ApiOperation("获取账户信息")
    public ApiResult<SysAccountQueryVo> getAccountInfo(@PathVariable("id") Long id) {
        SysAccount account = sysAccountService.getById(id);
        if (account == null) {
            return ApiResult.fail("用户不存在");
        }
        SysAccountQueryVo vo = new SysAccountQueryVo();
        BeanUtil.copyProperties(account, vo);
        List<Long> roleIds = sysAccountRoleService.lambdaQuery()
                .select(SysAccountRole::getRoleId)
                .eq(SysAccountRole::getAccountId, id)
                .list()
                .stream()
                .map(SysAccountRole::getRoleId)
                .distinct()
                .collect(Collectors.toList());
        if (!roleIds.isEmpty()) {
            List<SysRole> roles = sysRoleService.listByIds(roleIds);
            SysAccountRoleVo roleVo = new SysAccountRoleVo();
            roleVo.setAccountId(id);
            roleVo.setRoleIds(roleIds);
            roleVo.setRoleNames(roles == null ? new ArrayList<>() : roles.stream().map(SysRole::getRoleName).collect(Collectors.toList()));
            roleVo.setRoleCodes(roles == null ? new ArrayList<>() : roles.stream().map(SysRole::getRoleCode).collect(Collectors.toList()));
            vo.setAccountRole(roleVo);
        }
        return ApiResult.ok(vo);
    }

    @PostMapping("/update")
    @ApiOperation("更新账户")
    public ApiResult<Boolean> updateAccount(@RequestBody SysAccount account) {
        if (account == null || account.getId() == null) {
            return ApiResult.fail("id不能为空");
        }
        return ApiResult.ok(sysAccountService.updateById(account));
    }

    @PostMapping("/getPageList")
    @ApiOperation("账户分页列表")
    public ApiResult<Paging<SysAccountQueryVo>> getPageList(@RequestBody(required = false) SysAccountQueryParam param) {
        SysAccountQueryParam query = param == null ? new SysAccountQueryParam() : param;
        Page<SysAccount> page = sysAccountService.page(
                new Page<>(query.getCurrent(), query.getSize()),
                Wrappers.lambdaQuery(SysAccount.class)
                        .eq(StrUtil.isNotBlank(query.getAccountStatus()), SysAccount::getAccountStatus, query.getAccountStatus())
                        .eq(StrUtil.isNotBlank(query.getAccountType()), SysAccount::getAccountType, query.getAccountType())
                        .eq(StrUtil.isNotBlank(query.getProductVersion()), SysAccount::getProductVersion, query.getProductVersion())
                        .and(StrUtil.isNotBlank(query.getKeywords()), w -> w
                                .like(SysAccount::getAccountName, query.getKeywords())
                                .or().like(SysAccount::getPhone, query.getKeywords())
                                .or().like(SysAccount::getEmail, query.getKeywords())
                                .or().like(SysAccount::getUsername, query.getKeywords())
                        )
                        .orderByDesc(SysAccount::getId)
        );
        Page<SysAccountQueryVo> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(page.getRecords().stream().map(e -> {
            SysAccountQueryVo vo = new SysAccountQueryVo();
            BeanUtil.copyProperties(e, vo);
            return vo;
        }).collect(Collectors.toList()));
        return ApiResult.ok(new Paging<>(voPage));
    }

    @PutMapping("/update/avatar")
    @ApiOperation("更新头像")
    public ApiResult<Boolean> updateAvatar(@RequestBody @Valid ChangeAvatarParam param) {
        SysAccount account = new SysAccount();
        account.setId(param.getId());
        account.setAvatar(param.getAvatar());
        return ApiResult.ok(sysAccountService.updateById(account));
    }

    @PostMapping("/update/baseInfo")
    @ApiOperation("更新基本信息")
    public ApiResult<Boolean> updateBaseInfo(@RequestBody @Valid AccountBaseInfoUpdateParam param) {
        SysAccount account = new SysAccount();
        account.setId(param.getId());
        account.setAccountName(param.getAccountName());
        account.setPhone(param.getPhone());
        account.setEmail(param.getEmail());
        return ApiResult.ok(sysAccountService.updateById(account));
    }

    @PutMapping("/update/phone")
    @ApiOperation("更新手机号")
    public ApiResult<Boolean> updatePhone(@RequestBody @Valid ChangePhoneParam param) {
        if (StrUtil.isBlank(param.getCode())) {
            return ApiResult.fail("验证码不能为空");
        }
        String key = RedisKeyName.EMAIL_UPDATE_PHONE_KEY + param.getId();
        String cached = redisService.get(key);
        if (StrUtil.isBlank(cached)) {
            return ApiResult.fail("验证码已过期");
        }
        if (!StrUtil.equals(cached, param.getCode())) {
            return ApiResult.fail("验证码不正确");
        }
        redisService.remove(key);
        SysAccount account = new SysAccount();
        account.setId(param.getId());
        account.setPhone(param.getPhone());
        return ApiResult.ok(sysAccountService.updateById(account));
    }

    @GetMapping("/update/phone/emailCode/{id}")
    @ApiOperation("发送修改手机号邮箱验证码")
    public ApiResult<Boolean> sendUpdatePhoneEmailCode(@PathVariable("id") Long id) {
        if (id == null) {
            return ApiResult.fail("id不能为空");
        }
        SysAccount account = sysAccountService.getById(id);
        if (account == null) {
            return ApiResult.fail("用户不存在");
        }
        if (StrUtil.isBlank(account.getEmail())) {
            return ApiResult.fail("请先绑定邮箱");
        }
        String rateKey = RedisKeyName.SMS_RATE_LIMIT_KEY + RedisKeyName.EMAIL_UPDATE_PHONE_KEY + account.getEmail();
        if (StrUtil.isNotBlank(redisService.get(rateKey))) {
            return ApiResult.fail("发送过于频繁，请稍后再试");
        }
        String code = RandomUtil.randomNumbers(6);
        redisService.setEx(RedisKeyName.EMAIL_UPDATE_PHONE_KEY + id, code, 900, TimeUnit.SECONDS);
        redisService.setEx(rateKey, "1", 60, TimeUnit.SECONDS);
        String subject = "修改手机号验证码";
        String text = "验证码：" + code + "（15分钟内有效）";
        mailService.simple(account.getEmail(), subject, text);
        return ApiResult.ok(true);
    }

    

    @PostMapping("/send/email")
    @ApiOperation("发送邮箱验证码")
    public ApiResult<Boolean> sendEmail(@RequestBody(required = false) SendEmailParam param) {
        if (param == null || param.getId() == null) {
            return ApiResult.fail("id不能为空");
        }
        if (StrUtil.isBlank(param.getEmail())) {
            return ApiResult.fail("邮箱不能为空");
        }
        if (StrUtil.isBlank(param.getPassword())) {
            return ApiResult.fail("密码不能为空");
        }
        SysAccount account = sysAccountService.getById(param.getId());
        if (account == null) {
            return ApiResult.fail("用户不存在");
        }
        if (!StrUtil.equals(account.getPassword(), DigestUtils.md5Hex(param.getPassword()))) {
            return ApiResult.fail("密码不正确");
        }
        String rateKey = RedisKeyName.SMS_RATE_LIMIT_KEY + RedisKeyName.EMAIL_BIND_KEY + param.getEmail();
        if (StrUtil.isNotBlank(redisService.get(rateKey))) {
            return ApiResult.fail("发送过于频繁，请稍后再试");
        }
        String code = RandomUtil.randomNumbers(6);
        redisService.setEx(RedisKeyName.EMAIL_BIND_KEY + param.getEmail(), code, 900, TimeUnit.SECONDS);
        redisService.setEx(rateKey, "1", 60, TimeUnit.SECONDS);
        String subject = "邮箱验证码";
        String text = "验证码：" + code + "（15分钟内有效）";
        mailService.simple(param.getEmail(), subject, text);
        return ApiResult.ok(true);
    }

    @PutMapping("/update/email")
    @ApiOperation("更新邮箱")
    public ApiResult<Boolean> updateEmail(@RequestBody @Valid ChangeEmailParam param) {
        if (param.getId() == null) {
            return ApiResult.fail("id不能为空");
        }
        if (StrUtil.isBlank(param.getEmail())) {
            return ApiResult.fail("邮箱不能为空");
        }
        if (StrUtil.isBlank(param.getCode())) {
            return ApiResult.fail("验证码不能为空");
        }
        String key = RedisKeyName.EMAIL_BIND_KEY + param.getEmail();
        String cached = redisService.get(key);
        if (StrUtil.isBlank(cached)) {
            return ApiResult.fail("验证码已过期");
        }
        if (!StrUtil.equals(cached, param.getCode())) {
            return ApiResult.fail("验证码不正确");
        }
        redisService.remove(key);
        SysAccount account = new SysAccount();
        account.setId(param.getId());
        account.setEmail(param.getEmail());
        return ApiResult.ok(sysAccountService.updateById(account));
    }
}
