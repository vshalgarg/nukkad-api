package com.code.monks.nukkad.admin.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminLoginResponseDto {

    private String token;
    private Long userId;
    private String email;
    private List<String> roles;
    private List<String> permissions;
    private Map<String,String> userProfile;
}
