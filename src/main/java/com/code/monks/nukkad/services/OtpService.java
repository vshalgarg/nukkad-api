package com.code.monks.nukkad.services;

import com.code.monks.nukkad.auth.response.AuthSendOtpResponseDTO;
import com.code.monks.nukkad.auth.response.AuthUserAccountDeactivateResponseDTO;
import com.code.monks.nukkad.auth.response.AuthVerifyOtpResponseDTO;
import com.code.monks.nukkad.client.AuthRestClient;
import com.code.monks.nukkad.context.UserContextHolder;
import com.code.monks.nukkad.dto.request.SendOtpRequestDTO;
import com.code.monks.nukkad.dto.request.VerifyRequestDTO;
import com.code.monks.nukkad.dto.response.SendOtpResponseDTO;
import com.code.monks.nukkad.dto.response.UserAccountDeactivateResponseDTO;
import com.code.monks.nukkad.dto.response.VerifyOtpResponseDTO;
import com.code.monks.nukkad.entities.CustomerEntity;
import com.code.monks.nukkad.entities.StorekeeperEntity;
import com.code.monks.nukkad.entities.UserDeviceTokenEntity;
import com.code.monks.nukkad.enums.NotificationStatusEnum;
import com.code.monks.nukkad.enums.RoleEnum;
import com.code.monks.nukkad.repositories.CustomerRepository;
import com.code.monks.nukkad.repositories.StorekeeperRepository;
import com.code.monks.nukkad.repositories.UserDeviceTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@AllArgsConstructor
public class OtpService {

	private final AuthRestClient authRestClient;
	private final CustomerRepository customerRepository;
	private final StorekeeperRepository storekeeperRepository;
	private final UserDeviceTokenRepository userDeviceTokenRepository;
	private final NotificationStatusService notificationStatusService;

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
        log.info("roles : {}",roles);
		boolean firstTimeLogin = false;

		if (roles.contains(RoleEnum.CUSTOMER.name())) {
			firstTimeLogin = !customerRepository.existsById(userId);
		} else if (roles.contains(RoleEnum.STOREKEEPER.name())) {
			firstTimeLogin = !storekeeperRepository.existsById(userId);
		}

		// Initialize notification preference only if NOT first time login
//		if (!firstTimeLogin) {
//			try {
//				log.info("[VERIFY OTP] Existing user. Ensuring notification status is initialized...");
//				notificationStatusService.initializeStatusIfAbsent(userId, roles);
//			} catch (Exception e) {
//				log.error("[VERIFY OTP] Failed to initialize notification status for userId: {}", userId, e);
//			}
//		}

		// Save or update device token
		String deviceToken = verifyRequestDTO.getDeviceToken();
		log.debug("[VERIFY OTP] Received device token: {}", deviceToken);

		if (deviceToken != null && !deviceToken.isEmpty()) {
			Optional<UserDeviceTokenEntity> existingTokenOpt = Optional.empty();

			if (roles.contains(RoleEnum.CUSTOMER.name())) {
				log.debug("[VERIFY OTP] User has CUSTOMER role. Fetching token by customerId: {}", userId);
				existingTokenOpt = userDeviceTokenRepository.findByCustomerId(userId);
			} else if (roles.contains(RoleEnum.STOREKEEPER.name())) {
				log.debug("[VERIFY OTP] User has STOREKEEPER role. Fetching token by storeKeeperId: {}", userId);
				existingTokenOpt = userDeviceTokenRepository.findByStoreKeeperId(userId);
			}

			UserDeviceTokenEntity tokenEntity = existingTokenOpt.orElseGet(() -> {
				log.debug("[VERIFY OTP] No existing token found. Creating new UserDeviceTokenEntity");
				return new UserDeviceTokenEntity();
			});

			tokenEntity.setDeviceToken(deviceToken);
			log.debug("[VERIFY OTP] Device token set");

			// Set user ID based on role
			if (roles.contains(RoleEnum.CUSTOMER.name())) {
				tokenEntity.setCustomerId(userId);
				log.debug("[VERIFY OTP] Set customerId on token entity for userId: {}", userId);
			} else if (roles.contains(RoleEnum.STOREKEEPER.name())) {
				tokenEntity.setStoreKeeperId(userId);
				log.debug("[VERIFY OTP] Set storeKeeperId on token entity for userId: {}", userId);
			}

			userDeviceTokenRepository.save(tokenEntity);
			log.info("[VERIFY OTP] Device token saved/updated successfully for userId: {}", userId);
		} else {
			log.warn("[VERIFY OTP] Device token is null or empty for userId: {}", userId);
		}


		final int code = firstTimeLogin ? 1501 : 1502;
		log.info("[VERIFY OTP] OTP verified. userId: {}, firstTimeLogin: {}, code: {}", userId, firstTimeLogin, code);

		return new VerifyOtpResponseDTO(
				userId,
				authResponse.getUsername(),
				roles,
				authResponse.getToken(),
				authResponse.getStatus(),
				code
		);
	}

	public UserAccountDeactivateResponseDTO deactivateAccount() {
		String mobileNumber = UserContextHolder.getUser().getMobileNumber();
		log.info("[DEACTIVATE ACCOUNT] Request received to deactivate account for mobile number: {}", mobileNumber);

		AuthUserAccountDeactivateResponseDTO authResponse = authRestClient.callUserAccountDeactivateResponse(mobileNumber);
		log.info("[DEACTIVATE ACCOUNT] Response from Auth Service: {}", authResponse);

		String message = authResponse.getMessage();
		log.info("[DEACTIVATE ACCOUNT] Deactivation message: {}", message);

		return new UserAccountDeactivateResponseDTO(message);
	}


}

