package com.code.monks.nukkad.auth.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class AuthSendOtpRequestDTO {
    private String username;
    private Set<String> roles;
}




