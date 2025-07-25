package com.code.monks.nukkad.dto.request;

import lombok.Data;

@Data
public class VerifyRequestDTO {

    private String mobileNumber;
    private String otp;
    private String deviceToken;

}
