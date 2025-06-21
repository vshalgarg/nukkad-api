package com.code.monks.nukkad.auth.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthSendOtpRequestDTO {

    private String recipient;
    private String[] roles;


}




