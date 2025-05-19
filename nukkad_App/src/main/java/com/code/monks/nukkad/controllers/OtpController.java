package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.dto.request.SendOtpRequestDTO;
import com.code.monks.nukkad.dto.response.SendOtpResponseDTO;
import com.code.monks.nukkad.services.OtpService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

		if (response.getMessage() == null || response.getMessage().isBlank()) {
			log.error("OTP sending failed. Empty message returned.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new SendOtpResponseDTO(""));
		}

		log.info("OTP sent successfully.");
		return ResponseEntity.ok(response);
	}

}
