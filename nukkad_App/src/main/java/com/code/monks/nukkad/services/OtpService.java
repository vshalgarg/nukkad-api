package com.code.monks.nukkad.services;

import com.code.monks.nukkad.client.AuthRestClient;
import com.code.monks.nukkad.dto.SendOtpRequestDTO;
import com.code.monks.nukkad.dto.SendOtpResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
