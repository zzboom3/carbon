package com.carbon.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.bean.copier.ValueProvider;
import cn.hutool.core.bean.copier.provider.BeanValueProvider;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.carbon.auth.mapper.LoginMapper;
import com.carbon.auth.service.LoginService;
import com.carbon.auth.service.EmailCodeService;
import com.carbon.common.constants.AccountConstant;
import com.carbon.common.enums.ExpCodeEnum;
import com.carbon.common.exception.CommonBizException;
import com.carbon.common.redis.CommonRedisKey;
import com.carbon.common.redis.RedisService;
import com.carbon.common.utils.HttpContextUtils;
import com.carbon.common.utils.JwtUtil;
import com.carbon.common.utils.WxJwtUtils;
import com.carbon.domain.auth.param.ForgotPasswordParam;
import com.carbon.domain.auth.param.LoginParam;
import com.carbon.domain.auth.param.RegisterParam;
import com.carbon.domain.auth.vo.LoginInfoVo;
import com.carbon.domain.auth.vo.SecurityData;
import com.carbon.domain.common.ApiCode;
import com.carbon.domain.common.ApiResult;
import com.carbon.domain.common.constant.RedisKeyName;
import com.carbon.domain.common.constant.RocketDelayLevelConstant;
import com.carbon.domain.common.constant.RocketMqName;
import com.carbon.domain.mq.entity.OpenRegisterAccount;
import com.carbon.domain.system.api.SystemServiceApi;
import com.carbon.auth.entity.SysAccount;
import com.carbon.domain.system.param.ChangePasswordParam;
import com.carbon.domain.system.param.SysAccountParam;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.messaging.Message;
import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

/**
 * @author Li Jun
 */
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

	private static final Set<String> SUPER_ROLE_CODES = new HashSet<>(Arrays.asList("0200000009", "0200000010"));

	@Value("${carbon.security.permitAll:false}")
	private boolean permitAll;

	@Resource
	private LoginMapper loginMapper;
	@Autowired
	private RedisService redisService;
	@Autowired
	private EmailCodeService emailCodeService;
	@Autowired
	private SystemServiceApi systemServiceApi;
	@Resource
	private RocketMQTemplate mqTemplate;

	@Override
	public LoginInfoVo byLoginName(LoginParam loginParam) {

		String accountName = StrUtil.trimToEmpty(loginParam.getAccountName());
		String password = loginParam.getPassword();
		log.info("login accountName={}", accountName);

		SysAccount account = loginMapper.selectOne(Wrappers.lambdaQuery(SysAccount.class).eq(SysAccount::getAccountName, accountName));
		if (account == null) {
			throw new CommonBizException(ExpCodeEnum.SYSTEM_SECURITY_USER_NULL);
		}
		if (StrUtil.isNotEmpty(password) && !account.getPassword().equals(DigestUtils.md5Hex(password))) {
			throw new CommonBizException(ExpCodeEnum.SYSTEM_SECURITY_USER_PASSWORD_ERROR);
		}
		if (AccountConstant.ACCOUNT_STATUS_DISABLE.equals(account.getAccountStatus())) {
			throw new CommonBizException(ExpCodeEnum.SYS_ACCOUNT_DISABLE);
		}
		if (AccountConstant.ACCOUNT_STATUS_NO_OPENED.equals(account.getAccountStatus())) {
			throw new CommonBizException(ExpCodeEnum.SYS_ACCOUNT_NO_OPENED);
		}

		LoginInfoVo loginInfo;

		// 判断是否登录,如果已经登录直接返回
		loginInfo = getLoginInfoByRedis(account.getId());
		if (null != loginInfo){
			return loginInfo;
		}

		// 创建登录信息,并且保存到redis
		String token = JwtUtil.generateToken(account.getId(),account.getTenantId());
		loginInfo = new LoginInfoVo();
		loginInfo.setToken(token);
		loginInfo.setSecurityData(getSecurityData(account.getId()));
		saveLoginInfoByRedis(loginInfo);

		return loginInfo;
	}

	@Override
	public void logout(Long accountId) {
		removeLoginInfoByRedis(accountId);
	}

	@Override
	public void register(RegisterParam param) {
		emailCodeService.checkAndConsumeRegisterCode(param.getEmail(), param.getCode());
		SysAccountParam accountParam = new SysAccountParam();
		accountParam.setAccountName(param.getAccountName());
		accountParam.setPassword(param.getPassword());
		accountParam.setPhone(param.getPhone());
		accountParam.setEmail(param.getEmail());
		accountParam.setAccountStatus(AccountConstant.ACCOUNT_STATUS_NO_OPENED);
		accountParam.setAccountType("0380000001");//试用账户
		accountParam.setProductVersion("0400000001");//试用版
		accountParam.setRoleIds(CollUtil.newArrayList(9L));//角色ID
		ApiResult<Boolean> result = systemServiceApi.addSysAccount(accountParam);
		log.info("register:{}",JSONUtil.toJsonPrettyStr(result));
		if (ApiCode.SUCCESS.getCode() != result.getCode()){
			throw new CommonBizException(result.getMsg());
		}
		//发送MQ消息
		OpenRegisterAccount registerAccount = BeanUtil.fillBean(new OpenRegisterAccount(), new ValueProvider<String>() {
			@Override
			public Object value(String s, Type type) {
				return "";
			}
			@Override
			public boolean containsKey(String s) {
				return true;
			}
		}, CopyOptions.create());//创建空对象，null转""

		registerAccount.setAccountName(param.getAccountName());
		registerAccount.setProductVersion("试用版");
		registerAccount.setContactNumber(param.getPhone());
		registerAccount.setAccountState("未开户");

		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		registerAccount.setRegistrationTime(dtf.format(now));

        Message<OpenRegisterAccount> msg= MessageBuilder
                .withPayload(BeanUtil.copyProperties(registerAccount,OpenRegisterAccount.class)).build();
        mqTemplate.syncSend(RocketMqName.OpenRegisterAccount_MSG,msg,3000, RocketDelayLevelConstant.SECOND10);
	}

	@Override
	public void forgotPassword(ForgotPasswordParam param) {
		emailCodeService.checkAndConsumeForgotPasswordCode(param.getEmail(), param.getCode());
		SysAccount account = loginMapper.selectOne(Wrappers.lambdaQuery(SysAccount.class).eq(SysAccount::getEmail, param.getEmail()));
		if (account == null) {
			throw new CommonBizException(ExpCodeEnum.SYSTEM_SECURITY_USER_NULL);
		}
		//更新密码
		ChangePasswordParam changePasswordParam = new ChangePasswordParam();
		changePasswordParam.setId(account.getId());
		changePasswordParam.setOldPasswordMd5(account.getPassword());
		changePasswordParam.setNewPassword(param.getPassword());
		ApiResult result = systemServiceApi.updatePassword(changePasswordParam);
		if (ApiCode.SUCCESS.getCode() != result.getCode()){
			throw new CommonBizException(result.getMsg());
		}
	}

	@Override
	public Boolean isLogin(Long accountId){
		//判断 redisToken 是否失效,判断请求token是否和redis一致
		String requestToken = HttpContextUtils.getRequestToken();

		LoginInfoVo loginInfo = getLoginInfoByRedis(accountId);
		if (null == loginInfo){
			return false;
		}

		String redisToken = loginInfo.getToken();
		return !StrUtil.isEmpty(redisToken) && redisToken.equals(requestToken);
	}

	@Override
	public Boolean verify(String accountName) {
		return loginMapper.selectOne(new QueryWrapper<SysAccount>().eq("account_name", accountName)) == null;
	}

	@Override
	public ApiResult<Boolean> checkPermission(String checkUrl) {
		log.info("checkPermission-url:{}",checkUrl);
		//获取登录信息
		LoginInfoVo loginInfo = getLoginInfoByRedis(HttpContextUtils.getAccountId());
		if (loginInfo == null){
			return ApiResult.fail(ApiCode.UNAUTHORIZED);
		}
		if (permitAll) {
			return ApiResult.ok();
		}
		if (StrUtil.isBlank(checkUrl)) {
			return ApiResult.ok();
		}
		SecurityData securityData = loginInfo.getSecurityData();
		if (securityData == null) {
			return ApiResult.fail(ApiCode.UNAUTHORIZED);
		}

		if (securityData.getAccountName() != null && "admin".equalsIgnoreCase(securityData.getAccountName())) {
			return ApiResult.ok();
		}

		if (CollUtil.isNotEmpty(securityData.getRoleCodes())) {
			boolean isSuperRole = securityData.getRoleCodes().stream()
					.filter(StrUtil::isNotBlank)
					.anyMatch(SUPER_ROLE_CODES::contains);
			if (isSuperRole) {
				return ApiResult.ok();
			}
			boolean isAdmin = securityData.getRoleCodes().stream()
					.filter(StrUtil::isNotBlank)
					.anyMatch(r -> "admin".equalsIgnoreCase(r));
			if (isAdmin) {
				return ApiResult.ok();
			}
		}

		String normalizedUrl = checkUrl;
		String[] prefixes = new String[]{"/system/", "/assets/", "/trade/", "/authCenter/", "/workbench/", "/cmall/", "/bmall/"};
		for (String prefix : prefixes) {
			if (normalizedUrl.startsWith(prefix)) {
				normalizedUrl = "/" + normalizedUrl.substring(prefix.length());
				break;
			}
		}

		final String normalizedUrlFinal = normalizedUrl;

		if (normalizedUrlFinal.startsWith("/sysMenu/userMenus")) {
			return ApiResult.ok();
		}

		List<String> permissionCodes = securityData.getPermissionCodes();
		if (CollUtil.isEmpty(permissionCodes) || permissionCodes.stream().noneMatch(StrUtil::isNotBlank)) {
			return ApiResult.fail(ApiCode.NOT_PERMISSION);
		}

		boolean matched = permissionCodes.stream()
				.filter(StrUtil::isNotBlank)
				.map(p -> p.startsWith("/") ? p : ("/" + p))
				.anyMatch(p -> checkUrl.startsWith(p) || normalizedUrlFinal.startsWith(p));
		return matched ? ApiResult.ok() : ApiResult.fail(ApiCode.NOT_PERMISSION);
		
	}

	@Override
	public SecurityData getSecurityData(Long accountId) {
		SecurityData data = loginMapper.getSecurityDataAccountId(accountId);
		if (data == null){
			return null;
		}
		if (StrUtil.isBlank(data.getRoleCodeStr())) {
			data.setRoleCodes(new ArrayList<>());
		} else {
			data.setRoleCodes(Arrays.asList(data.getRoleCodeStr().split(",")));
		}
		if (StrUtil.isBlank(data.getPermissionCodesStr())) {
			data.setPermissionCodes(new ArrayList<>());
		} else {
			data.setPermissionCodes(Arrays.asList(data.getPermissionCodesStr().split(",")));
		}
		return data;
	}

	/**
	 * redis 获取登录信息
	 * 如果 redis 中的token不合法则返回空，用户未登录
	 */
	private LoginInfoVo getLoginInfoByRedis(Long accountId) {
		String json = redisService.get(String.format(CommonRedisKey.LOGIN_USER, accountId));
		if (StrUtil.isEmpty(json)) {
			return null;
		}
		LoginInfoVo loginInfo = new Gson().fromJson(json, LoginInfoVo.class);
		//校验redis中的token
		if (!JwtUtil.isValidToken(loginInfo.getToken())){
			return null;
		}
		return loginInfo;
	}

	/**
	 * redis 存储登录信息
	 */
	private void saveLoginInfoByRedis(LoginInfoVo loginInfoVo) {
		Long accountId = loginInfoVo.getSecurityData().getAccountId();
		redisService.setEx(String.format(CommonRedisKey.LOGIN_USER, accountId),
				JSONUtil.toJsonStr(loginInfoVo),JwtUtil.EXPIRE_SECOND, TimeUnit.SECONDS);
	}

	/**
	 * 清除登录信息
	 */
	private void removeLoginInfoByRedis(Long accountId){
		redisService.remove(String.format(CommonRedisKey.LOGIN_USER, accountId));
	}

}
