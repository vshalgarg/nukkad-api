package com.code.monks.nukkad.client;

import com.code.monks.nukkad.dto.User;
import com.code.monks.nukkad.dto.request.SendOtpRequestDTO;
import com.code.monks.nukkad.dto.request.VerifyRequestDTO;
import com.code.monks.nukkad.dto.response.SendOtpResponseDTO;
import com.code.monks.nukkad.dto.response.VerifyResponseDTO;
import com.code.monks.nukkad.enums.RoleEnum;
import com.code.monks.nukkad.exception.ExternalServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
@Slf4j
@Component
public class AuthRestClient {

	private final GenericRestClient genericRestClient;

	@Autowired
	public AuthRestClient(GenericRestClient genericRestClient) {
		this.genericRestClient = genericRestClient;
	}

	private final String baseUrl = "http://localhost:8080/";
	private final String otpUrl = baseUrl+ "/api/auth/otp";

	public SendOtpResponseDTO callOtpResponse(SendOtpRequestDTO otpRequest) {
		return genericRestClient.postForEntity(otpUrl, otpRequest, SendOtpResponseDTO.class, "OTP matched API failed");
	}

	public VerifyResponseDTO callVerifyOtpResponse(VerifyRequestDTO verifyRequestDTO)
	{
		return genericRestClient.postForEntity(otpUrl, verifyRequestDTO, VerifyResponseDTO.class, "OTP verify API failed");
	}

	public User validateToken(String token){

		Map<String, Object> map = genericRestClient.callApi(baseUrl+"validation",token);

		if (map == null || map.isEmpty()){
			throw new ExternalServiceException("Token validation failed or empty response. ");
		}
		User user = new User();
		user.setId((Long) map.get("userId"));
		user.setRole((RoleEnum) map.get("role"));
		return user;
	}
}
