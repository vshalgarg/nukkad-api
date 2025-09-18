package com.code.monks.nukkad.auth.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthVerifyFirebaseTokenRequestDTO {
    private String phoneNumber;
    private String token;
}
