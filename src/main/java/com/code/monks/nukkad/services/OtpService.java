package com.code.monks.nukkad.services;

import com.code.monks.nukkad.auth.response.AuthSendOtpResponseDTO;
import com.code.monks.nukkad.auth.response.AuthVerifyOtpResponseDTO;
import com.code.monks.nukkad.client.AuthRestClient;
import com.code.monks.nukkad.dto.request.SendOtpRequestDTO;
import com.code.monks.nukkad.dto.request.VerifyRequestDTO;
import com.code.monks.nukkad.dto.response.SendOtpResponseDTO;
import com.code.monks.nukkad.dto.response.VerifyOtpResponseDTO;
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
		AuthSendOtpResponseDTO authResponse=authRestClient.callOtpResponse(sendOtpRequestDTO);
		return new SendOtpResponseDTO(authResponse.getMessage());
	}

	public VerifyOtpResponseDTO verifyOtp(VerifyRequestDTO verifyRequestDTO)
	{
		String mobileNumber = verifyRequestDTO.getMobileNumber();
		String otp = verifyRequestDTO.getOtp();
		log.info("Verifying OTP for mobile :{} , OTP :{}" , mobileNumber,otp);
		AuthVerifyOtpResponseDTO authResponse= authRestClient.callVerifyOtpResponse(verifyRequestDTO);
		return new VerifyOtpResponseDTO(authResponse.getUserId(),
				                     authResponse.getUsername(),
				                     authResponse.getRoles(),
				                     authResponse.getToken());
	}
}

