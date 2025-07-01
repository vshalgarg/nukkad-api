package com.code.monks.nukkad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class VerifyOtpResponseDTO {
    private Long userId;
    private String phoneNumber;
    private List<String> roles;
    private String token;
}
