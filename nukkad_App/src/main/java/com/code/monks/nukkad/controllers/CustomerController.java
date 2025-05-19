package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.dto.request.CreateCustomerRequestDTO;
import com.code.monks.nukkad.dto.response.CreateCustomerResponseDTO;
import com.code.monks.nukkad.services.CustomerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.code.monks.nukkad.constants.UrlConstants.*;

@RestController
@RequestMapping(CUSTOMER.BASE)
@Slf4j
@AllArgsConstructor
public class CustomerController {


	private  final CustomerService customerService;


	@PostMapping(CUSTOMER.CREATE)
	public ResponseEntity<CreateCustomerResponseDTO> createCustomer(@Valid @RequestBody CreateCustomerRequestDTO customerRequestDTO) {
		log.info("Received request to createCustomer: {}", customerRequestDTO);
		CreateCustomerResponseDTO response = customerService.saveCustomerData(customerRequestDTO);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PutMapping(CUSTOMER.UPDATE)
	public ResponseEntity<CreateCustomerResponseDTO> updateCustomer(@PathVariable Long id,
																	@Valid @RequestBody CreateCustomerRequestDTO customerRequestDTO) {
		log.info("Received request to updateCustomer with id {}: {}", id, customerRequestDTO);
		CreateCustomerResponseDTO response = customerService.updateCustomer(id, customerRequestDTO);
		return ResponseEntity.ok(response);
	}

}
