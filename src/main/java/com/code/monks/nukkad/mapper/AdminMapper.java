package com.code.monks.nukkad.mapper;

import com.code.monks.nukkad.admin.response.AdminLoginResponseDto;
import com.code.monks.nukkad.auth.response.AuthAdminLoginResponseDTO;

public class AdminMapper {

    public static AdminLoginResponseDto toAdminLoginResponseDto(AuthAdminLoginResponseDTO authResponse) {
        AdminLoginResponseDto dto = new AdminLoginResponseDto();
        dto.setToken(authResponse.getToken());
        dto.setEmail(authResponse.getUsername());
        dto.setUserId(authResponse.getUserId());
        dto.setPermissions(authResponse.getPermissions());
        dto.setRoles(authResponse.getRoles());
        return dto;
    }
}
