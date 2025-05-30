package com.code.monks.nukkad.services;

import com.code.monks.nukkad.client.AuthRestClient;
import com.code.monks.nukkad.dto.request.SendOtpRequestDTO;
import com.code.monks.nukkad.dto.response.SendOtpResponseDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class OtpService {

	private final AuthRestClient authRestClient;

	public SendOtpResponseDTO sendOtp(SendOtpRequestDTO sendOtpRequestDTO) {
		String mobileNumber = sendOtpRequestDTO.getMobileNumber();
		log.info("Requesting OTP for mobile: {}", mobileNumber);
		return authRestClient.callOtpResponse(sendOtpRequestDTO);
	}

}

