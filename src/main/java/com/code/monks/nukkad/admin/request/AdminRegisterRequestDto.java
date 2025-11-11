package com.code.monks.nukkad.admin.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminRegisterRequestDto {

    private String email;
    private String password;
    private String[] roles;
}
