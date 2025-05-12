package com.neepanlokInfotech.nukkad_App.services;

import com.neepanlokInfotech.nukkad_App.client.AuthRestClient;
import com.neepanlokInfotech.nukkad_App.dto.SendOtpRequestDTO;
import com.neepanlokInfotech.nukkad_App.dto.SendOtpResponseDTO;
import com.neepanlokInfotech.nukkad_App.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Service
@Slf4j
public class OtpService {

    @Autowired
    private AuthRestClient authRestClient;

    public SendOtpResponseDTO sendOtp(SendOtpRequestDTO sendOtpRequestDTO) {
        String mobileNumber = sendOtpRequestDTO.getMobileNumber();
        log.info("Requesting OTP for mobile: {}", mobileNumber);
       return authRestClient.callOtpResponse(sendOtpRequestDTO);
    }
}
