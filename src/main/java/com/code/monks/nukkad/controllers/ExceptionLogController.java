package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.dto.request.ExceptionLogRequestDTO;
import com.code.monks.nukkad.dto.response.ExceptionLogResponseDTO;
import com.code.monks.nukkad.services.ExceptionLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.code.monks.nukkad.constants.UrlConstants.EXCEPTION_LOG.ADD_EXCEPTION_LOG;
import static com.code.monks.nukkad.constants.UrlConstants.EXCEPTION_LOG.BASE;

@RestController
@RequestMapping(BASE)
@RequiredArgsConstructor
@Slf4j
public class ExceptionLogController {

    private final ExceptionLogService exceptionLogService;

    @PostMapping(ADD_EXCEPTION_LOG)
    public ResponseEntity<ExceptionLogResponseDTO> logException(@RequestBody ExceptionLogRequestDTO requestDTO) {
        log.info("Received request to log exception: message={}, path={}",
                requestDTO.getMessage(), requestDTO.getRequestPath());

        ExceptionLogResponseDTO responseDTO = exceptionLogService.saveExceptionLog(requestDTO);
 
        log.info("Successfully saved exception log with message: {}", responseDTO.getMessage());

        return ResponseEntity.ok(responseDTO);
    }

}
