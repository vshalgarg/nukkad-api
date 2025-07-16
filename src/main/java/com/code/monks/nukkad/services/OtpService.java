package com.code.monks.nukkad.services;

import com.code.monks.nukkad.auth.response.AuthSendOtpResponseDTO;
import com.code.monks.nukkad.auth.response.AuthVerifyOtpResponseDTO;
import com.code.monks.nukkad.client.AuthRestClient;
import com.code.monks.nukkad.dto.request.SendOtpRequestDTO;
import com.code.monks.nukkad.dto.request.VerifyRequestDTO;
import com.code.monks.nukkad.dto.response.SendOtpResponseDTO;
import com.code.monks.nukkad.dto.response.VerifyOtpResponseDTO;
import com.code.monks.nukkad.enums.RoleEnum;
import com.code.monks.nukkad.repositories.CustomerRepository;
import com.code.monks.nukkad.repositories.StorekeeperRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
@AllArgsConstructor
public class OtpService {

	private final AuthRestClient authRestClient;

	private final CustomerRepository customerRepository;

	private final StorekeeperRepository storekeeperRepository;

	public SendOtpResponseDTO sendLoginOtp(SendOtpRequestDTO sendOtpRequestDTO) {
		String mobileNumber = sendOtpRequestDTO.getMobileNumber();

		log.info("Requesting OTP for mobile: {}", mobileNumber);
		AuthSendOtpResponseDTO authResponse=authRestClient.callOtpResponse(sendOtpRequestDTO);
		return new SendOtpResponseDTO(authResponse.getMessage());
	}

	public VerifyOtpResponseDTO verifyLoginOtp(VerifyRequestDTO verifyRequestDTO) {
		String mobileNumber = verifyRequestDTO.getMobileNumber();
		String otp = verifyRequestDTO.getOtp();
		log.info("[VERIFY OTP] Verifying OTP for mobile: {}, OTP: {}", mobileNumber, otp);

		AuthVerifyOtpResponseDTO authResponse = authRestClient.callVerifyOtpResponse(verifyRequestDTO);

		Long userId = authResponse.getUserId();
		List<String> roles = authResponse.getRoles();

		boolean firstTimeLogin = false;

		if (roles.contains(RoleEnum.CUSTOMER.name())) {
			firstTimeLogin = !customerRepository.existsById(userId);
		} else if (roles.contains(RoleEnum.STOREKEEPER.name())) {
			firstTimeLogin = !storekeeperRepository.existsById(userId);
		}

		final int code = firstTimeLogin ? 1501 : 1502;
		log.info("[VERIFY OTP] OTP verified. userId: {}, firstTimeLogin: {}, code: {}", userId, firstTimeLogin, code);

		return new VerifyOtpResponseDTO(
				userId,
				authResponse.getUsername(),
				roles,
				authResponse.getToken(),
				code
		);
	}



//	public SendOtpResponseDTO sendDeleteOtp(SendOtpRequestDTO sendOtpRequestDTO) {
//		String mobileNumber = sendOtpRequestDTO.getMobileNumber();
//		log.info("[SEND DELETE OTP] Sending delete OTP to mobile: {}", mobileNumber);
//
//		AuthSendOtpResponseDTO authResponse = authRestClient.callOtpResponse(sendOtpRequestDTO);
//		return new SendOtpResponseDTO(authResponse.getMessage());
//	}
//
//	public VerifyOtpResponseDTO verifyDeleteOtp(VerifyRequestDTO verifyRequestDTO) {
//		String mobileNumber = verifyRequestDTO.getMobileNumber();
//		String otp = verifyRequestDTO.getOtp();
//		log.info("[VERIFY DELETE OTP] Verifying OTP for mobile: {}, OTP: {}", mobileNumber, otp);
//
//		AuthVerifyOtpResponseDTO authResponse = authRestClient.callVerifyOtpResponse(verifyRequestDTO);
//
//		return new VerifyOtpResponseDTO(
//				authResponse.getUserId(),
//				authResponse.getUsername(),
//				authResponse.getRoles(),
//				authResponse.getToken()
//		);
//	}


}

