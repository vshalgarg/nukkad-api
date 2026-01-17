package com.code.monks.nukkad.dto.request;

import lombok.Data;

@Data
public class VerifyOtpRequestDto {
    private String phoneNumber;
    private String verificationCode;
}
