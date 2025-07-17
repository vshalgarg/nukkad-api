package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.dto.request.SendOtpRequestDTO;
import com.code.monks.nukkad.dto.request.VerifyRequestDTO;
import com.code.monks.nukkad.dto.response.SendOtpResponseDTO;
import com.code.monks.nukkad.dto.response.VerifyOtpResponseDTO;
import com.code.monks.nukkad.services.OtpService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.code.monks.nukkad.constants.UrlConstants.OTP;

@Slf4j
@RestController
@RequestMapping(OTP.BASE)
@AllArgsConstructor
public class OtpController {

	private final OtpService otpService;

	@PostMapping(OTP.SEND_LOGIN_OTP)
	public ResponseEntity<SendOtpResponseDTO> sendLoginOtp(@Valid  @RequestBody SendOtpRequestDTO request) {
		log.info("[SEND OTP] Request received for mobile: {}", request.getMobileNumber());

		SendOtpResponseDTO response = otpService.sendLoginOtp(request);

		log.info("[SEND OTP] OTP sent successfully to mobile: {}", request.getMobileNumber());
		return ResponseEntity.ok(response);
	}

	@PostMapping(OTP.VERIFY_LOGIN_OTP)
	public ResponseEntity<VerifyOtpResponseDTO> verifyLoginOtp(@RequestBody VerifyRequestDTO request) {
		log.info("[VERIFY OTP] Request received for mobile: {}", request.getMobileNumber());

		VerifyOtpResponseDTO response = otpService.verifyLoginOtp(request);

		log.info("[VERIFY OTP] Verification result for mobile {}:", request.getMobileNumber());
		return ResponseEntity.ok(response);
	}

//	@PostMapping(OTP.SEND_DELETE_OTP)
//	public ResponseEntity<SendOtpResponseDTO> sendDeleteOtp(@Valid @RequestBody SendOtpRequestDTO request) {
//		log.info("[SEND DELETE OTP] Request received for mobile: {}", request.getMobileNumber());
//		SendOtpResponseDTO response = otpService.sendDeleteOtp(request);
//		return ResponseEntity.ok(response);
//	}
//
//	@PostMapping(OTP.VERIFY_DELETE_OTP)
//	public ResponseEntity<VerifyOtpResponseDTO> verifyDeleteOtp(@RequestBody VerifyRequestDTO request) {
//		log.info("[VERIFY DELETE OTP] Request received for mobile: {}", request.getMobileNumber());
//		VerifyOtpResponseDTO response = otpService.verifyDeleteOtp(request);
//		return ResponseEntity.ok(response);
//	}

}
