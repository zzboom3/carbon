package com.carbon.auth.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.carbon.auth.service.EmailCodeService;
import com.carbon.common.exception.CommonBizException;
import com.carbon.common.redis.RedisService;
import com.carbon.domain.common.constant.RedisKeyName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailCodeServiceImpl implements EmailCodeService {

    private final RedisService redisService;
    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;

    @Override
    public void sendRegisterCode(String email) {
        sendCode(email, RedisKeyName.EMAIL_REGISTER_KEY, "注册验证码", "注册验证码：%s（5分钟内有效）");
    }

    @Override
    public void sendForgotPasswordCode(String email) {
        sendCode(email, RedisKeyName.EMAIL_FORGOT_PASSWORD_KEY, "重置密码验证码", "重置密码验证码：%s（5分钟内有效）");
    }

    @Override
    public void checkAndConsumeRegisterCode(String email, String code) {
        checkAndConsume(email, code, RedisKeyName.EMAIL_REGISTER_KEY);
    }

    @Override
    public void checkAndConsumeForgotPasswordCode(String email, String code) {
        checkAndConsume(email, code, RedisKeyName.EMAIL_FORGOT_PASSWORD_KEY);
    }

    private void sendCode(String email, String keyPrefix, String subject, String textTemplate) {
        if (StrUtil.isBlank(email)) {
            throw new CommonBizException("邮箱不能为空");
        }
        if (StrUtil.isBlank(mailProperties.getHost())
                || StrUtil.isBlank(mailProperties.getUsername())
                || StrUtil.isBlank(mailProperties.getPassword())
                || "xxxxxx".equalsIgnoreCase(StrUtil.trim(mailProperties.getHost()))
                || "xxxxxx".equalsIgnoreCase(StrUtil.trim(mailProperties.getUsername()))
                || "xxxxxx".equalsIgnoreCase(StrUtil.trim(mailProperties.getPassword()))) {
            throw new CommonBizException("邮件服务未配置");
        }
        String rateKey = RedisKeyName.SMS_RATE_LIMIT_KEY + keyPrefix + email;
        if (StrUtil.isNotBlank(redisService.get(rateKey))) {
            throw new CommonBizException("发送过于频繁，请稍后再试");
        }
        String code = RandomUtil.randomNumbers(6);
        redisService.setEx(keyPrefix + email, code, 300, TimeUnit.SECONDS);
        redisService.setEx(rateKey, "1", 60, TimeUnit.SECONDS);

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(mailProperties.getUsername());
        msg.setTo(email);
        msg.setSubject(subject);
        msg.setText(String.format(textTemplate, code));
        try {
            mailSender.send(msg);
        } catch (Exception e) {
            log.error("发送邮件失败 email={}", email, e);
            throw new CommonBizException("发送失败");
        }
    }

    private void checkAndConsume(String email, String code, String keyPrefix) {
        if (StrUtil.isBlank(email) || StrUtil.isBlank(code)) {
            throw new CommonBizException("邮箱或验证码为空");
        }
        String redisKey = keyPrefix + email;
        String cached = redisService.get(redisKey);
        if (StrUtil.isBlank(cached)) {
            throw new CommonBizException("验证码已过期");
        }
        if (!StrUtil.equals(cached, StrUtil.trim(code))) {
            throw new CommonBizException("验证码不正确");
        }
        redisService.remove(redisKey);
    }
}
