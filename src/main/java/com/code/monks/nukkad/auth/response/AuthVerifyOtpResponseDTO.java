package com.code.monks.nukkad.auth.response;

import com.code.monks.nukkad.enums.AuthUserStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthVerifyOtpResponseDTO {
    private Long userId;
    private String username;
    private List<String> roles;
    private String token;
    private AuthUserStatusEnum status;
}
