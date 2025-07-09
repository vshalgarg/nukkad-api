package com.code.monks.nukkad.client;

import com.code.monks.nukkad.auth.request.AuthSendOtpRequestDTO;
import com.code.monks.nukkad.auth.request.AuthTokenRequestDto;
import com.code.monks.nukkad.auth.request.AuthVerifyOtpRequestDTO;
import com.code.monks.nukkad.auth.response.AuthSendOtpResponseDTO;
import com.code.monks.nukkad.auth.response.AuthTokenResponseDto;
import com.code.monks.nukkad.auth.response.AuthVerifyOtpResponseDTO;
import com.code.monks.nukkad.dto.User;
import com.code.monks.nukkad.dto.request.SendOtpRequestDTO;
import com.code.monks.nukkad.dto.request.VerifyRequestDTO;
import com.code.monks.nukkad.enums.RoleEnum;
import com.code.monks.nukkad.exception.ExternalServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

	@Value("${auth.otpVerify.url}")
	private String verifyOtpUrl;

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
			AuthSendOtpResponseDTO response = genericRestClient.postForEntity(url, authDto, headers, AuthSendOtpResponseDTO.class);
			log.info("[OTP SEND] Successfully sent OTP to mobile: {}", otpRequest.getMobileNumber());
			return response;
		} catch (Exception ex) {
			log.error("[OTP SEND] Failed to send OTP to mobile: {}. Error: {}", otpRequest.getMobileNumber(), ex.getMessage());
			throw ex;
		}
	}

//	public AuthVerifyOtpResponseDTO callVerifyOtpResponse(VerifyRequestDTO requestDTO) {
//		String url = authHost + verifyOtpUrl;
//		AuthVerifyOtpRequestDTO authDto = new AuthVerifyOtpRequestDTO(requestDTO.getMobileNumber(), requestDTO.getOtp());
//		Map<String, String> headers = new HashMap<>();
//		updateHeadersForClientNameAndSecret(headers);
//
//		log.info("[OTP VERIFY] Verifying OTP for mobile: {}", requestDTO.getMobileNumber());
//
////		try {
////			AuthVerifyOtpResponseDTO response = genericRestClient.postForEntity(url, authDto, headers, AuthVerifyOtpResponseDTO.class);
////			log.info("[OTP VERIFY] OTP verified successfully for mobile: {}", requestDTO.getMobileNumber());
////			return response;
////		} catch (Exception ex) {
////			log.error("[OTP VERIFY] Failed to verify OTP for mobile: {}. Error: {}", requestDTO.getMobileNumber(), ex.getMessage());
////			throw ex;
////		}
//		return genericRestClient.postForEntity(url, authDto, headers, AuthVerifyOtpResponseDTO.class);
//
//	}

	public AuthVerifyOtpResponseDTO callVerifyOtpResponse(VerifyRequestDTO requestDTO) {
	String url = authHost + verifyOtpUrl;
	AuthVerifyOtpRequestDTO authDto = new AuthVerifyOtpRequestDTO(requestDTO.getMobileNumber(), requestDTO.getOtp());
	Map<String, String> headers = new HashMap<>();
	updateHeadersForClientNameAndSecret(headers);

	log.info("[OTP VERIFY] Verifying OTP for mobile: {}", requestDTO.getMobileNumber());

	try {
		AuthVerifyOtpResponseDTO response = genericRestClient.postForEntity(
				url, authDto, headers, AuthVerifyOtpResponseDTO.class
		);

		if (response.getUserId() == null || response.getToken() == null) {
			String rawResponse = genericRestClient.postForEntity(url, authDto, headers, String.class);
			String message = rawResponse.replaceAll(".*\"message\"\\s*:\\s*\"([^\"]+)\".*", "$1");
			String code = rawResponse.replaceAll(".*\"responseCode\"\\s*:\\s*(\\d+).*", "$1");

			throw new ExternalServiceException(
					String.format("{\"message\":\"%s\",\"responseCode\":%s}", message, code)
			);
		}

		return response;

	} catch (Exception ex) {
		log.error("[OTP VERIFY] Failed to verify OTP for mobile: {}. Error: {}", requestDTO.getMobileNumber(), ex.getMessage());
		throw ex;
	}
}

	public User validateToken(AuthTokenRequestDto authDto) {
		String url = authHost + validateUrl;
		Map<String, String> headers = new HashMap<>();
		updateHeadersForClientNameAndSecret(headers);

		try {
			AuthTokenResponseDto authResponse = genericRestClient.postForEntity(url, authDto, headers, AuthTokenResponseDto.class);

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

	private void updateHeadersForClientNameAndSecret(Map<String, String> headers) {
		headers.put("clientName", authClientName);
		headers.put("clientSecret", authClientSecret);
	}
}








