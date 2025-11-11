package com.code.monks.nukkad.auth.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthAdminRegisterRequestDTO {
    private String username;
    private String password;
    private String[] roles;
}

