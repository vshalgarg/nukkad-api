package com.code.monks.nukkad.auth.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthTokenResponseDto {
    private Long userId;
    private String username;
}
