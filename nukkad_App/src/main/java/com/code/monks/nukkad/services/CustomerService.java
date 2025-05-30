package com.code.monks.nukkad.services;

import com.code.monks.nukkad.client.AuthRestClient;
import com.code.monks.nukkad.context.RequestContextHolder;
import com.code.monks.nukkad.dto.request.CreateCustomerRequestDTO;
import com.code.monks.nukkad.dto.response.CreateCustomerResponseDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class CustomerService {

    private final AuthRestClient authRestClient;


	public CreateCustomerResponseDTO saveCustomerData(CreateCustomerRequestDTO requestDTO) {
		    long customerId= RequestContextHolder.getCustomer().getCustomerId();
			return authRestClient.callSaveCustomerApi(customerId,requestDTO);
	}


	public CreateCustomerResponseDTO updateCustomer( CreateCustomerRequestDTO requestDTO) {
		long customerId= RequestContextHolder.getCustomer().getCustomerId();
		return authRestClient.callUpdateCustomerApi(customerId,requestDTO);
	}

}
