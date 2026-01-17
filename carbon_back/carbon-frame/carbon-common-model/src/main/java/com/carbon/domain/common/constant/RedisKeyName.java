package com.carbon.domain.common.constant;

/**
 * Date: 2022-03-26
 * Time: 11:38
 *
 * @author li jun
 */
public class RedisKeyName {

    /**
     * 字典类型列表
     */
    public static final String SYS_DICT_KEY = "sys:dict:";

    /**
     * 字典类型列表
     */
    public static final String ACCOUNT_NAME_CACHE = "account:name:";

    /**
     * 验证码发送限流
     */
    public static final String SMS_RATE_LIMIT_KEY = "sms:rateLimit:";

    /**
     * 邮箱验证码
     */
    public static final String EMAIL_BIND_KEY = "email:bind:";

    /**
     * 邮箱验证码-注册
     */
    public static final String EMAIL_REGISTER_KEY = "email:register:";

    /**
     * 邮箱验证码-忘记密码
     */
    public static final String EMAIL_FORGOT_PASSWORD_KEY = "email:forgotPassword:";

    /**
     * 邮箱验证码-修改手机号
     */
    public static final String EMAIL_UPDATE_PHONE_KEY = "email:updatePhone:";
    /**
     * 碳文章列表
     *
     */
    public static final String CA_ARTICLE_LIST_KEY = "ca:articleList:";

}
