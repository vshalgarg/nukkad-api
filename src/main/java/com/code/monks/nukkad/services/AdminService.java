package com.code.monks.nukkad.services;

import com.code.monks.nukkad.admin.request.AdminLoginRequestDto;
import com.code.monks.nukkad.admin.request.AdminRegisterRequestDto;
import com.code.monks.nukkad.admin.response.AdminLoginResponseDto;
import com.code.monks.nukkad.admin.response.AdminRegisterResponseDto;
import com.code.monks.nukkad.dto.request.ItemExcelDTO;

import java.util.List;

public interface AdminService {

    AdminLoginResponseDto login(AdminLoginRequestDto loginRequestDto);
    AdminRegisterResponseDto register(AdminRegisterRequestDto registerRequestDto);
    void bulkCreateFromExcel(List<ItemExcelDTO> dtos);
}
