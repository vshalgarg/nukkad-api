package com.code.monks.nukkad.admin.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminLoginRequestDto {

    private String email;
    private String password;
}
