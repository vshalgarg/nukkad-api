package com.code.monks.nukkad.auth.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthTokenResponseDto {
    private Long userId;
    private String username;
    private List<String> roles;
}
