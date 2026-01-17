package com.code.monks.nukkad.auth.request;

import lombok.Data;

@Data
public class AuthVerifyOtpRequestDto {

    private String phoneNumber;
    private String verificationCode;
}
