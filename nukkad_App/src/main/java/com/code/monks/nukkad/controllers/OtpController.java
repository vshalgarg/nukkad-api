package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.dto.request.SendOtpRequestDTO;
import com.code.monks.nukkad.dto.request.VerifyRequestDTO;
import com.code.monks.nukkad.dto.response.SendOtpResponseDTO;
import com.code.monks.nukkad.dto.response.VerifyResponseDTO;
import com.code.monks.nukkad.services.OtpService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.code.monks.nukkad.constants.UrlConstants.OTP;



@RestController
@RequestMapping(OTP.BASE)
@AllArgsConstructor
@Slf4j
public class  OtpController {

	private final OtpService otpService;

	@PostMapping(OTP.SENDOTP)
	public ResponseEntity<SendOtpResponseDTO> sendOtp(@RequestBody SendOtpRequestDTO sendOtpRequestDTO) {
		log.info("Received request to send OTP: {}", sendOtpRequestDTO);

		SendOtpResponseDTO response = otpService.sendOtp(sendOtpRequestDTO);


		log.info("OTP sent successfully.");
		return ResponseEntity.ok(response);
	}

	@PostMapping(OTP.VERIFYOTP)
	public ResponseEntity<VerifyResponseDTO> verifyOTP(@RequestBody VerifyRequestDTO dto)
	{
		log.info("Received OTP verification request for mobile :{}", dto.getMobileNumber());
		VerifyResponseDTO verifyResponseDTO=otpService.verifyDTO(dto);
		if(verifyResponseDTO == null)
		{
			log.warn("OTP verification failed: No response from service for mobile :{}", dto.getMobileNumber());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new VerifyResponseDTO(false,"Verification failed. Please try again"));
		}
		log.info("OTP verification result for mobile {}:{}",dto.getMobileNumber(), verifyResponseDTO.getMessage());
		return ResponseEntity.ok(verifyResponseDTO);
	}
}
