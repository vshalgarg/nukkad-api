package com.code.monks.nukkad.services;

import com.code.monks.nukkad.client.AuthRestClient;
import com.code.monks.nukkad.dto.SendOtpRequestDTO;
import com.code.monks.nukkad.dto.SendOtpResponseDTO;
import com.code.monks.nukkad.dto.request.VerifyRequestDTO;
import com.code.monks.nukkad.dto.response.VerifyResponseDTO;
import com.code.monks.nukkad.entities.OtpEntity;
import com.code.monks.nukkad.repositories.OtpRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

//import static com.code.monks.nukkad.services.OtpService.Expiry;

@Service
@Slf4j
@AllArgsConstructor
public class OtpService {

	private static final long EXPIRY_SECONDS = 300;
	private AuthRestClient authRestClient;

	private OtpRepository otpRepository;

	public SendOtpResponseDTO sendOtp(SendOtpRequestDTO sendOtpRequestDTO) {
		String mobileNumber = sendOtpRequestDTO.getMobileNumber();
		log.info("Requesting OTP for mobile: {}", mobileNumber);
		return authRestClient.callOtpResponse(sendOtpRequestDTO);
	}
	public VerifyResponseDTO verifyDTO(VerifyRequestDTO dto) {
		String mobile = dto.getMobileNumber();
		String inputOtp = dto.getOtp();
		log.info("Received OTP verification request for mobile: {}", mobile);

		Optional<OtpEntity> optional = otpRepository.findTopByMobileNumberOrderByCreatedAtDesc(mobile);

		if (optional.isEmpty()) {
			log.warn("No OTP record found for mobile: {}", mobile);
			return new VerifyResponseDTO("Fail", "Mobile Number is not found");
		}

		OtpEntity otpStored = optional.get();
		LocalDateTime now = LocalDateTime.now();

		if (otpStored.getCreatedAt().plusSeconds(EXPIRY_SECONDS).isBefore(now)) {
			log.warn("OTP for mobile {} has expired", mobile);
			return new VerifyResponseDTO("Fail", "Otp is Expired");
		}

		if (!otpStored.getOtp().equals(inputOtp)) {
			log.warn("Invalid OTP entered for mobile {}. Expected: {}, Provided: {}", mobile, otpStored.getOtp(), inputOtp);
			return new VerifyResponseDTO("Fail", "Invalid OTP");
		}

		otpStored.setVerified(true);
		otpStored.setMessage("Otp Verified");
		otpRepository.save(otpStored);

		log.info("OTP successfully verified for mobile: {}", mobile);
		return new VerifyResponseDTO("Success", "Otp verified successfully");
	}
}
