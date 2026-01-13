package com.carbon.system.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carbon.common.api.Paging;
import com.carbon.domain.common.ApiResult;
import com.carbon.domain.system.param.ChangePasswordParam;
import com.carbon.domain.system.param.SysAccountParam;
import com.carbon.domain.system.vo.SysAccountModelVo;
import com.carbon.system.entity.SysAccount;
import com.carbon.system.param.*;
import com.carbon.system.vo.SysAccountQueryVo;
import com.carbon.system.common.BaseController;
import com.carbon.system.service.SysAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import javax.annotation.Resource;
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
        SysAccount account = new SysAccount();
        account.setId(param.getId());
        account.setPhone(param.getPhone());
        return ApiResult.ok(sysAccountService.updateById(account));
    }

    @GetMapping("/update/code/{phone}")
    @ApiOperation("发送手机号验证码")
    public ApiResult<Boolean> sendPhoneCode(@PathVariable("phone") String phone) {
        return ApiResult.ok(true);
    }

    @PostMapping("/send/email")
    @ApiOperation("发送邮箱验证码")
    public ApiResult<Boolean> sendEmail(@RequestBody(required = false) SendEmailParam param) {
        return ApiResult.ok(true);
    }
}
