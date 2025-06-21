package com.code.monks.nukkad.dto.request;

import com.code.monks.nukkad.enums.RoleEnum;
import lombok.Data;

@Data
public class VerifyRequestDTO {

    private String mobileNumber;
    private String otp;

}
