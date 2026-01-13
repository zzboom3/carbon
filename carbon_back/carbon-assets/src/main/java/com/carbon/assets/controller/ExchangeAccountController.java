package com.carbon.assets.controller;

import com.carbon.assets.param.ExchangeAccountBindingParam;
import com.carbon.assets.service.ExchangeAccountService;
import com.carbon.assets.param.ExchangeAccountQueryParam;
import com.carbon.assets.entity.ExchangeAccount;
import com.carbon.assets.common.BaseController;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carbon.common.api.Paging;
import com.carbon.domain.common.ApiResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import cn.hutool.core.util.StrUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.annotation.Resource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;



/**
 * <p>
 * 交易账户  前端控制器
 * </p>
 *
 * @author Li Jun
 * @since 2021-08-16
 */
@Slf4j
@RestController
@RequestMapping("/exchangeAccount")
@Api(value = "交易账户 模块", tags = {"交易账户 模块"})
public class ExchangeAccountController extends BaseController {

    @Resource
    private ExchangeAccountService exchangeAccountService;

    @PostMapping("/getPageList")
    @ApiOperation(value = "交易账户分页列表", notes = "交易账户分页列表")
    public ApiResult<Paging<ExchangeAccount>> getPageList(@Valid @RequestBody(required = false) ExchangeAccountQueryParam param) {
        ExchangeAccountQueryParam query = param == null ? new ExchangeAccountQueryParam() : param;
        Page<ExchangeAccount> page = exchangeAccountService.page(
                new Page<>(query.getCurrent(), query.getSize()),
                Wrappers.lambdaQuery(ExchangeAccount.class)
                        .like(StrUtil.isNotBlank(query.getKeyWords()), ExchangeAccount::getAccountName, query.getKeyWords())
                        .orderByDesc(ExchangeAccount::getId)
        );
        return ApiResult.ok(new Paging<>(page));
    }

    @GetMapping("/info/{id}")
    @ApiOperation(value = "交易账户详情", notes = "交易账户详情")
    public ApiResult<ExchangeAccount> info(@PathVariable("id") Long id) {
        return ApiResult.ok(exchangeAccountService.getById(id));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增交易账户", notes = "新增交易账户")
    public ApiResult<Boolean> add(@Valid @RequestBody ExchangeAccount data) {
        return ApiResult.ok(exchangeAccountService.save(data));
    }

    @PutMapping("/binding")
    @ApiOperation(value = "绑定交易账户", notes = "绑定交易账户")
    public ApiResult<Boolean> binding(@Valid @RequestBody ExchangeAccountBindingParam param) {
        if (param == null || param.getAccountId() == null) {
            return ApiResult.fail("accountId不能为空");
        }
        ExchangeAccount account = new ExchangeAccount();
        account.setId(param.getAccountId());
        account.setCarbonExchangeId(param.getCarbonExchangeId());
        account.setCookie(param.getCookie());
        account.setBindingTime(new java.util.Date());
        return ApiResult.ok(exchangeAccountService.updateById(account));
    }

    @PutMapping("/unbind/{id}")
    @ApiOperation(value = "解绑交易账户", notes = "解绑交易账户")
    public ApiResult<Boolean> unbind(@PathVariable("id") Long id) {
        ExchangeAccount account = new ExchangeAccount();
        account.setId(id);
        account.setCookie(null);
        return ApiResult.ok(exchangeAccountService.updateById(account));
    }

    @PostMapping("/uploadCredential")
    @ApiOperation(value = "上传凭证文件", notes = "上传凭证文件")
    public Map<String, Object> uploadCredential(@RequestParam("file") MultipartFile file) throws Exception {
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
        res.put("msg", "http://localhost:9091/assets/upload/assets/" + dateDir + "/" + safeName);
        return res;
    }
}
