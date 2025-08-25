package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.enums.AuthUserStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class VerifyOtpResponseDTO {
    private Long userId;
    private String mobileNumber;
    private List<String> roles;
    private List<String> permissions;
    private String token;
    private AuthUserStatusEnum status;
    private int firstTimeLogin;
}
