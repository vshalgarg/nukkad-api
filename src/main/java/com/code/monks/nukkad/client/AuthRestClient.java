package com.code.monks.nukkad.client;

import com.code.monks.nukkad.admin.request.AdminLoginRequestDto;
import com.code.monks.nukkad.admin.request.AdminRegisterRequestDto;
import com.code.monks.nukkad.auth.request.*;
import com.code.monks.nukkad.auth.response.*;
import com.code.monks.nukkad.dto.User;
import com.code.monks.nukkad.dto.request.SendOtpRequestDTO;
import com.code.monks.nukkad.enums.RoleEnum;
import com.code.monks.nukkad.exception.ExternalServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AuthRestClient {

	private final GenericRestClient genericRestClient;

	@Value("${auth.host}")
	private String authHost;

	@Value("${auth.otp.url}")
	private String sendOtpUrl;

	@Value("${auth.client.name}")
	private String authClientName;

	@Value("${auth.client.secret}")
	private String authClientSecret;

	@Value("${auth.validateToken.url}")
	private String validateUrl;

	@Value("${auth.Verify.firebaseToken.url}")
	private String verifyFirebaseUrl;

	@Value("${auth.userAccountDeactivate.url}")
	private String userAccountDeactivateUrl;

	@Value("${auth.login.url}")
	private String adminLoginURL;

	@Value("${auth.register.url}")
	private String adminRegisterURL;

	@Autowired
	public AuthRestClient(GenericRestClient genericRestClient) {
		this.genericRestClient = genericRestClient;
	}

	public AuthSendOtpResponseDTO callOtpResponse(SendOtpRequestDTO otpRequest) {
		String url = authHost + sendOtpUrl;
		Set<String> roles = new HashSet<>();
		roles.add(otpRequest.getRole());
		AuthSendOtpRequestDTO authDto = new AuthSendOtpRequestDTO(otpRequest.getMobileNumber(), roles);
		Map<String, String> headers = new HashMap<>();
		updateHeadersForClientNameAndSecret(headers);

		log.info("[OTP SEND] Initiating OTP send request to: {} for mobile: {}", url, otpRequest.getMobileNumber());

		try {
			AuthSendOtpResponseDTO response = genericRestClient.postForEntity(url, authDto, headers, AuthSendOtpResponseDTO.class,HttpMethod.POST);
			log.info("[OTP SEND] Successfully sent OTP to mobile: {}", otpRequest.getMobileNumber());
			return response;
		} catch (Exception ex) {
			log.error("[OTP SEND] Failed to send OTP to mobile: {}. Error: {}", otpRequest.getMobileNumber(), ex.getMessage());
			throw ex;
		}
	}

	public AuthVerifyOtpResponseDTO callVerifyOtpResponse(String mobileNumber,String firebaseToken) {
	String url = authHost + verifyFirebaseUrl;
	AuthVerifyFirebaseTokenRequestDTO authDto = new AuthVerifyFirebaseTokenRequestDTO(mobileNumber,firebaseToken);

		log.debug("[VERIFY FIREBASE] Outgoing payload: mobile={} token={}",
				mobileNumber, firebaseToken != null ? firebaseToken.substring(0, 10) : "null");

		Map<String, String> headers = new HashMap<>();
	updateHeadersForClientNameAndSecret(headers);

		log.info("[VERIFY FIREBASE] Verifying Firebase token with Auth service. mobile: {}", mobileNumber);

		try {
			AuthVerifyOtpResponseDTO response = genericRestClient.postForEntity(
					url, authDto, headers, AuthVerifyOtpResponseDTO.class, HttpMethod.POST
			);

			if (response.getUserId() == null || response.getToken() == null) {
				throw new ExternalServiceException("Invalid Firebase token response from Auth");
			}

			return response;

		} catch (Exception ex) {
			log.error("[VERIFY FIREBASE] Failed to verify Firebase token. Error: {}", ex.getMessage());
			throw ex;
		}
}

	public AuthUserAccountDeactivateResponseDTO callUserAccountDeactivateResponse(String firebaseToken) {
		String url = authHost + userAccountDeactivateUrl;
		log.info("[AUTH SERVICE] Calling deactivation endpoint: {}", url);

		AuthUserAccountDeactivateRequestDTO authDto = new AuthUserAccountDeactivateRequestDTO(firebaseToken);
		log.info("[AUTH SERVICE] Request payload: {}", authDto);

		Map<String, String> headers = new HashMap<>();
		updateHeadersForClientNameAndSecret(headers);
		log.info("[AUTH SERVICE] Request headers: {}", headers);

		AuthUserAccountDeactivateResponseDTO response = genericRestClient.postForEntity(
				url, authDto, headers, AuthUserAccountDeactivateResponseDTO.class, HttpMethod.PUT);

		log.info("[AUTH SERVICE] Response received: {}", response);
		return response;
	}


	public User validateToken(AuthTokenRequestDto authDto) {
		String url = authHost + validateUrl;
		Map<String, String> headers = new HashMap<>();
		updateHeadersForClientNameAndSecret(headers);

		try {
			AuthTokenResponseDto authResponse = genericRestClient.postForEntity(url, authDto, headers, AuthTokenResponseDto.class,HttpMethod.POST);

			if (authResponse == null || authResponse.getUserId() == null) {
				throw new ExternalServiceException("Token validation failed or empty response.");
			}

			User user = new User();
			user.setId(authResponse.getUserId());
			user.setMobileNumber(authResponse.getUsername());
			List<RoleEnum> roleEnums = authResponse.getRoles().stream()
					.map(roleStr ->  {
						try {
							String cleanRole = roleStr.replaceFirst("^ROLE_", "");
							return RoleEnum.valueOf(cleanRole.toUpperCase());
						} catch (IllegalArgumentException e) {
							log.warn("[TOKEN VALIDATION] Unknown role string received: '{}'", roleStr);
							return null; // filter it out later
						}
					})
					.filter(Objects::nonNull)
					.collect(Collectors.toList());

			user.setRoles(roleEnums);

			log.info("[TOKEN VALIDATION] Token validated. User ID: {}, Mobile: {}", user.getId(), user.getMobileNumber());
			return user;

		} catch (Exception ex) {
			throw ex;
		}
	}

	public AuthAdminLoginResponseDTO callAdminLoginApi(AdminLoginRequestDto loginRequestDto) {
		String url = authHost + adminLoginURL;
		log.info("[AUTH SERVICE] Calling Admin Login endpoint: {}", url);

		AuthAdminLoginRequestDTO authRequestDTO =
				new AuthAdminLoginRequestDTO(loginRequestDto.getEmail(), loginRequestDto.getPassword());

		Map<String, String> headers = new HashMap<>();
		updateHeadersForClientNameAndSecret(headers);

		try {
			AuthAdminLoginResponseDTO authLoginResponse = genericRestClient.postForEntity(
					url, authRequestDTO, headers, AuthAdminLoginResponseDTO.class, HttpMethod.POST);

			log.info("[AUTH SERVICE] Admin login successful for email: {}", loginRequestDto.getEmail());
			return authLoginResponse;

		} catch (Exception e) {
			log.error("[AUTH SERVICE] Error calling Admin Login API: {}", e.getMessage(), e);
			throw e;
		}
	}

	public AuthAdminRegisterResponseDTO  callAdminRegisterApi(AdminRegisterRequestDto registerRequestDto) {
		String url = authHost + adminRegisterURL;
		log.info("[AUTH SERVICE] Calling Admin Register endpoint: {}", url);

		AuthAdminRegisterRequestDTO authRequestDTO = new AuthAdminRegisterRequestDTO(
				registerRequestDto.getEmail(),
				registerRequestDto.getPassword(),
				registerRequestDto.getRoles()
		);

		Map<String, String> headers = new HashMap<>();
		updateHeadersForClientNameAndSecret(headers);

		try {
			AuthAdminRegisterResponseDTO  response = genericRestClient.postForEntity(
					url, authRequestDTO, headers, AuthAdminRegisterResponseDTO.class, HttpMethod.POST);

			log.info("[AUTH SERVICE] Admin registered successfully for email: {}", registerRequestDto.getEmail());
			return response;

		} catch (Exception e) {
			log.error("[AUTH SERVICE] Error calling Admin Register API: {}", e.getMessage(), e);
			throw e;
		}
	}

	private void updateHeadersForClientNameAndSecret(Map<String, String> headers) {
		headers.put("clientName", authClientName);
		headers.put("clientSecret", authClientSecret);
	}
}








