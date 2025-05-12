package com.neepanlokInfotech.nukkad_App.client;

import com.neepanlokInfotech.nukkad_App.dto.SendOtpRequestDTO;
import com.neepanlokInfotech.nukkad_App.dto.SendOtpResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthRestClient {

    private final GenericRestClient genericRestClient;

    @Autowired
    public AuthRestClient(GenericRestClient genericRestClient) {
        this.genericRestClient = genericRestClient;
    }

    @Value("${external.otp.api.url}")
    private String url;

    public SendOtpResponseDTO callOtpResponse(SendOtpRequestDTO otpRequest){
        return genericRestClient.postForEntity(url,otpRequest,SendOtpResponseDTO.class,"OTP matched API failed");
    }
} 
