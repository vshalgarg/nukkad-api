package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.admin.request.AdminLoginRequestDto;
import com.code.monks.nukkad.admin.request.AdminRegisterRequestDto;
import com.code.monks.nukkad.admin.response.AdminLoginResponseDto;
import com.code.monks.nukkad.admin.response.AdminRegisterResponseDto;
import com.code.monks.nukkad.constants.UrlConstants;
import com.code.monks.nukkad.services.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.code.monks.nukkad.constants.UrlConstants.ADMIN.LOGIN;
import static com.code.monks.nukkad.constants.UrlConstants.ADMIN.REGISTER;

@RestController
@RequestMapping(UrlConstants.ADMIN.BASE)
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final AdminService adminService;

    @PostMapping(LOGIN)
    public ResponseEntity<AdminLoginResponseDto> login(@RequestBody AdminLoginRequestDto loginRequestDto) {
        log.info("[ADMIN CONTROLLER] Login endpoint hit for email: {}", loginRequestDto.getEmail());
        AdminLoginResponseDto responseDto = adminService.login(loginRequestDto);
        log.info("[ADMIN CONTROLLER] Login successful for email: {}", loginRequestDto.getEmail());
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping(REGISTER)
    public ResponseEntity<AdminRegisterResponseDto> register(@RequestBody AdminRegisterRequestDto request) {
        AdminRegisterResponseDto response = adminService.register(request);
        return ResponseEntity.ok(response);
    }
}
