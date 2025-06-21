package com.code.monks.nukkad.auth.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AuthVerifyOtpResponseDTO {
    private Long userId;
    private String username;
    private List<String> roles;
    private String token;
}
