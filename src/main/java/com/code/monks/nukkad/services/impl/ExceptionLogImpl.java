package com.code.monks.nukkad.services.impl;

import com.code.monks.nukkad.dto.request.ExceptionLogRequestDTO;
import com.code.monks.nukkad.dto.response.ExceptionLogResponseDTO;
import com.code.monks.nukkad.entities.ExceptionLogEntity;
import com.code.monks.nukkad.exception.UnhandledException;
import com.code.monks.nukkad.repositories.ExceptionLogRepository;
import com.code.monks.nukkad.services.ExceptionLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.code.monks.nukkad.enums.ResponseErrorCodes.UNHANDLED_EXCEPTION;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExceptionLogImpl implements ExceptionLogService {

    private final ExceptionLogRepository exceptionLogRepository;

    @Override
    public ExceptionLogResponseDTO saveExceptionLog(ExceptionLogRequestDTO requestDTO) {
        log.info("Attempting to save exception log: message={}, requestPath={}", requestDTO.getMessage(),
                requestDTO.getRequestPath());
        try {
            ExceptionLogEntity entity = new ExceptionLogEntity();
            entity.setMessage(requestDTO.getMessage());
            entity.setRequestPath(requestDTO.getRequestPath());

            ExceptionLogEntity savedEntity = exceptionLogRepository.save(entity);

            log.info("Successfully saved exception log with ID: {}", savedEntity.getId());

            ExceptionLogResponseDTO responseDTO = new ExceptionLogResponseDTO();
            responseDTO.setMessage("Exception log saved with id : " + savedEntity.getId());

            return responseDTO;
        } catch (Exception e) {
            log.error("Failed to save exception log. Message: {}, RequestPath: {}. Error: {}",
                    requestDTO.getMessage(),
                    requestDTO.getRequestPath(),
                    e.getMessage(),
                    e);
            throw new UnhandledException(UNHANDLED_EXCEPTION, e);
        }
    }

}
