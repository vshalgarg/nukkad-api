package com.neepanlokInfotech.nukkad_App.services;

import com.neepanlokInfotech.nukkad_App.dto.SendOtpRequestDTO;
import com.neepanlokInfotech.nukkad_App.dto.SendOtpResponseDTO;
import com.neepanlokInfotech.nukkad_App.entities.OtpEntity;
import com.neepanlokInfotech.nukkad_App.exception.ResourceNotFoundException;
import com.neepanlokInfotech.nukkad_App.mapper.OtpMapper;
import com.neepanlokInfotech.nukkad_App.repositories.OtpRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Service
@Slf4j
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String OTP_API_URL = "https://example.com/api/generateOtp?mobile={mobile}";

    public SendOtpResponseDTO sendOtp(SendOtpRequestDTO sendOtpRequestDTO) {
        String mobileNumber = sendOtpRequestDTO.getMobileNumber();
        log.info("Requesting OTP for mobile: {}", mobileNumber);

        try {
            // Call external OTP API
            ResponseEntity<SendOtpResponseDTO> response = restTemplate.getForEntity(OTP_API_URL, SendOtpResponseDTO.class, mobileNumber);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                String otpCode = response.getBody().getMessage();
                log.info("Received OTP '{}' for mobile {}", otpCode, mobileNumber);

                // Save to DB
                OtpEntity otpEntity = new OtpEntity();
                otpEntity.setMobileNumber(mobileNumber);
                otpEntity.setOtp(otpCode);
                otpEntity.setCreatedAt(LocalDateTime.now());
                otpEntity.setMessage("OTP sent successfully");

                otpRepository.save(otpEntity);
                return OtpMapper.otpResponseDTODto(otpEntity);
            } else {
                log.error("Failed to get OTP from external service for mobile {}", mobileNumber);
                throw new ResourceNotFoundException("Failed to get OTP from external provider.");
            }

        } catch (Exception ex) {
            log.error("Exception while calling OTP API for mobile {}: {}", mobileNumber, ex.getMessage(), ex);
            throw new ResourceNotFoundException("OTP generation failed");
        }
    }
}
