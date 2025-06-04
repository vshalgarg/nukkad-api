package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.constants.UrlConstants;
import com.code.monks.nukkad.dto.request.CreateCustomerRequestDTO;
import com.code.monks.nukkad.dto.response.CreateCustomerResponseDTO;
import com.code.monks.nukkad.services.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(UrlConstants.CUSTOMER.BASE)
@RequiredArgsConstructor
public class CustomerController {

	private final CustomerService customerService;

	@PostMapping(UrlConstants.CUSTOMER.CREATE)
	public ResponseEntity<CreateCustomerResponseDTO> createCustomer(@Valid @RequestBody CreateCustomerRequestDTO dto) {
		CreateCustomerResponseDTO saveCustomerInDb = customerService.createCustomer(dto);
		return new  ResponseEntity<>(saveCustomerInDb, HttpStatus.CREATED);
	}


	@PutMapping(UrlConstants.CUSTOMER.UPDATE)
	public ResponseEntity<CreateCustomerResponseDTO> updateCustomer(
			@PathVariable Long id,
			@Valid @RequestBody CreateCustomerRequestDTO dto
	) {
		CreateCustomerResponseDTO updateCustomer = customerService.updateCustomer(id, dto);
		return new ResponseEntity<>(updateCustomer,HttpStatus.CREATED);
	}
}
