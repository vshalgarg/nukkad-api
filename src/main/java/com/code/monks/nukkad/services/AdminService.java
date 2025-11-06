package com.code.monks.nukkad.services;

import com.code.monks.nukkad.admin.request.AdminLoginRequestDto;
import com.code.monks.nukkad.admin.response.AdminLoginResponseDto;

public interface AdminService {

    AdminLoginResponseDto login(AdminLoginRequestDto loginRequestDto);
}
