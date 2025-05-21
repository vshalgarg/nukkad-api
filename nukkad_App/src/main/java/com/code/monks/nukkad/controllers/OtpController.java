package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.dto.SendOtpRequestDTO;
import com.code.monks.nukkad.dto.SendOtpResponseDTO;
import com.code.monks.nukkad.services.OtpService;
import com.code.monks.nukkad.dto.request.VerifyRequestDTO;
import com.code.monks.nukkad.dto.response.VerifyResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.code.monks.nukkad.constants.UrlConstants.OTP;
import static com.code.monks.nukkad.constants.UrlConstants.SEND_OTP;

@RestController
@RequestMapping(OTP)
public class  OtpController {

	@Autowired
	private OtpService otpService;

	// to sent otp
	@PostMapping(SEND_OTP)
	public ResponseEntity<SendOtpResponseDTO> sendOtp(@RequestBody SendOtpRequestDTO sendOtpRequestDTO) {
		SendOtpResponseDTO sendOtpResponseDTO = otpService.sendOtp(sendOtpRequestDTO);
		if (sendOtpResponseDTO.getMessage().equalsIgnoreCase("")) {
			return ResponseEntity.ok(sendOtpResponseDTO);
		}
		else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new SendOtpResponseDTO(""));
		}
	}

	// to verify otp
	@PostMapping("/verify")
	public ResponseEntity<VerifyResponseDTO> verifyOTP(@RequestBody VerifyRequestDTO dto)
	{
		VerifyResponseDTO verifyResponseDTO=otpService.verifyDTO(dto);
		return ResponseEntity.ok(verifyResponseDTO);
	}

}
