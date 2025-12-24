package com.code.monks.nukkad.services;

import com.code.monks.nukkad.dto.request.ExceptionLogRequestDTO;
import com.code.monks.nukkad.dto.response.ExceptionLogResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface ExceptionLogService {
    ExceptionLogResponseDTO saveExceptionLog(ExceptionLogRequestDTO requestDTO);
}
