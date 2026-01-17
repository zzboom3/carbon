package com.carbon.auth.service;

public interface EmailCodeService {
    void sendRegisterCode(String email);

    void sendForgotPasswordCode(String email);

    void checkAndConsumeRegisterCode(String email, String code);

    void checkAndConsumeForgotPasswordCode(String email, String code);
}

