package com.code.monks.nukkad.services;

import com.code.monks.nukkad.dto.request.ExceptionLogRequestDTO;
import com.code.monks.nukkad.dto.response.ExceptionLogResponseDTO;

public interface ExceptionLogService {
    ExceptionLogResponseDTO saveExceptionLog(ExceptionLogRequestDTO requestDTO);
}
