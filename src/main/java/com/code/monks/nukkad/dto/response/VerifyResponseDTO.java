package com.code.monks.nukkad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class VerifyResponseDTO {
    private Long userId;
    private String mobileNumber;
    private List<String> roles;
    private String token;
}
