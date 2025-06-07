package com.code.monks.nukkad.services;

import com.code.monks.nukkad.client.AuthRestClient;
import com.code.monks.nukkad.dto.request.SendOtpRequestDTO;
import com.code.monks.nukkad.dto.request.VerifyRequestDTO;
import com.code.monks.nukkad.dto.response.SendOtpResponseDTO;
import com.code.monks.nukkad.dto.response.VerifyResponseDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

	public VerifyResponseDTO verifyDTO(VerifyRequestDTO verifyRequestDTO)
	{
		String mobileNumber = verifyRequestDTO.getMobileNumber();
		String otp = verifyRequestDTO.getOtp();
		log.info("Verifying OTP for mobile :{} , OTP :{}" , mobileNumber,otp);
		return authRestClient.callVerifyOtpResponse(verifyRequestDTO);
	}
}

