package com.code.monks.nukkad.services.impl;

import com.code.monks.nukkad.admin.request.AdminLoginRequestDto;
import com.code.monks.nukkad.admin.request.AdminRegisterRequestDto;
import com.code.monks.nukkad.admin.response.AdminLoginResponseDto;
import com.code.monks.nukkad.admin.response.AdminRegisterResponseDto;
import com.code.monks.nukkad.auth.response.AuthAdminLoginResponseDTO;
import com.code.monks.nukkad.auth.response.AuthAdminRegisterResponseDTO;
import com.code.monks.nukkad.client.AuthRestClient;
import com.code.monks.nukkad.exception.UnhandledException;
import com.code.monks.nukkad.mapper.AdminMapper;
import com.code.monks.nukkad.services.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.code.monks.nukkad.enums.ResponseErrorCodes.UNHANDLED_EXCEPTION;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final AuthRestClient authRestClient;
    @Override
    public AdminLoginResponseDto login(AdminLoginRequestDto loginRequestDto) {

        log.info("[ADMIN SERVICE] Login request received for email: {}", loginRequestDto.getEmail());
            // Call Auth Service
            AuthAdminLoginResponseDTO authLoginResponse = authRestClient.callAdminLoginApi(loginRequestDto);
            log.info("[ADMIN SERVICE] Successfully authenticated admin: {}", loginRequestDto.getEmail());

            AdminLoginResponseDto responseDto = AdminMapper.toAdminLoginResponseDto(authLoginResponse);
            log.debug("[ADMIN SERVICE] Login response prepared for admin: {}", loginRequestDto.getEmail());
            return responseDto;
    }

    @Override
    public AdminRegisterResponseDto register(AdminRegisterRequestDto registerRequestDto) {
        log.info("[ADMIN SERVICE] Register request received for email: {}", registerRequestDto.getEmail());

        AuthAdminRegisterResponseDTO authResponse = authRestClient.callAdminRegisterApi(registerRequestDto);

        String message = switch (authResponse.getStatus()) {
            case ACTIVE -> "Admin registered successfully and activated.";
            case INACTIVE -> "Admin registered successfully but inactive.";
        };

        log.info("[ADMIN SERVICE] Registration completed for username: {}, Status: {}",
                registerRequestDto.getEmail(), authResponse.getStatus());

        return new AdminRegisterResponseDto(message);
    }
}
