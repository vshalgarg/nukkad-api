package com.code.monks.nukkad.client;

import com.code.monks.nukkad.dto.SendOtpRequestDTO;
import com.code.monks.nukkad.dto.SendOtpResponseDTO;
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

	public SendOtpResponseDTO callOtpResponse(SendOtpRequestDTO otpRequest) {
		return genericRestClient.postForEntity(url, otpRequest, SendOtpResponseDTO.class, "OTP matched API failed");
	}

}
