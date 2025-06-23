package com.code.monks.nukkad.auth.request;

import com.code.monks.nukkad.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthVerifyOtpRequestDTO {
    private String phoneNumber;
    private String verificationCode;

}
