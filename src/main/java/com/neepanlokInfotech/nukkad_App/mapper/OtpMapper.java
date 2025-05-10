package com.neepanlokInfotech.nukkad_App.mapper;

import com.neepanlokInfotech.nukkad_App.dto.SendOtpRequestDTO;
import com.neepanlokInfotech.nukkad_App.dto.SendOtpResponseDTO;
import com.neepanlokInfotech.nukkad_App.entities.OtpEntity;

public class OtpMapper {
    public static SendOtpRequestDTO otpRequestDTODto(OtpEntity entity) {
        SendOtpRequestDTO dto = new SendOtpRequestDTO();
        dto.setMobileNumber(entity.getMobileNumber());
        return dto;
    }
    public static SendOtpResponseDTO otpResponseDTODto(OtpEntity entity) {
        SendOtpResponseDTO dto = new SendOtpResponseDTO();
        dto.setMessage(entity.getMessage());
        return dto;
    }
}
