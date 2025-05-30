package com.code.monks.nukkad.client;

import com.code.monks.nukkad.dto.Customer;
import com.code.monks.nukkad.dto.request.CreateCustomerRequestDTO;
import com.code.monks.nukkad.dto.request.SendOtpRequestDTO;
import com.code.monks.nukkad.dto.request.VerifyRequestDTO;
import com.code.monks.nukkad.dto.response.CreateCustomerResponseDTO;
import com.code.monks.nukkad.dto.response.SendOtpResponseDTO;
import com.code.monks.nukkad.dto.response.VerifyResponseDTO;
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

	private final String baseUrl = "http://localhost:8080";
	private final String otpUrl = baseUrl+ "/api/auth/otp";
    private final String saveUrl=baseUrl+"/api/auth/save";
	private final String updateUrl = baseUrl+"/api/auth/update";

	public SendOtpResponseDTO callOtpResponse(SendOtpRequestDTO otpRequest) {
		return genericRestClient.postForEntity(otpUrl, otpRequest, SendOtpResponseDTO.class, "OTP matched API failed");
	}

	public CreateCustomerResponseDTO callSaveCustomerApi(Long customerId, CreateCustomerRequestDTO requestDTO){
		return genericRestClient.postForEntity(saveUrl,requestDTO, CreateCustomerResponseDTO.class,"Customer save API failed");
	}

	public CreateCustomerResponseDTO callUpdateCustomerApi(Long customerId,CreateCustomerRequestDTO requestDTO){
		return genericRestClient.postForEntity(updateUrl,requestDTO , CreateCustomerResponseDTO.class,"Customer update API failed");
	}

	public Customer validateToken(String token){

		Map<String, Object> map = genericRestClient.callApi(baseUrl+"validation",token);

		if (map == null || map.isEmpty()){
			throw new ExternalServiceException("Token validation failed or empty response. ");
		}
		Customer customer = new Customer();
		customer.setCustomerId((Long) map.get("customerId"));
		return customer;

	}

	public VerifyResponseDTO callVerifyOtpResponse(VerifyRequestDTO verifyRequestDTO)
	{
		return genericRestClient.postForEntity(otpUrl, verifyRequestDTO, VerifyResponseDTO.class, "OTP verify API failed");
	}
}
