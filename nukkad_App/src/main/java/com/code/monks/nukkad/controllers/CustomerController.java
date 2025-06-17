package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.dto.request.CreateCustomerRequestDTO;
import com.code.monks.nukkad.dto.response.CreateCustomerResponseDTO;
import com.code.monks.nukkad.entities.CustomerEntity;
import com.code.monks.nukkad.entities.StorekeeperEntity;
import com.code.monks.nukkad.services.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(CUSTOMER.BASE)
@RequiredArgsConstructor
public class CustomerController {

	private final CustomerService customerService;

	@PostMapping(CUSTOMER.CREATE)
	public ResponseEntity<CreateCustomerResponseDTO> createCustomer(@Valid @RequestBody CreateCustomerRequestDTO dto) {
		log.info("[CREATE CUSTOMER] Request received: {}", dto);
		CreateCustomerResponseDTO response = customerService.createCustomer(dto);
		log.info("[CREATE CUSTOMER] Customer created with ID: {}", response.getId());
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PutMapping(CUSTOMER.UPDATE)
	public ResponseEntity<CreateCustomerResponseDTO> updateCustomer(@Valid @RequestBody CreateCustomerRequestDTO dto) {
		log.info("[UPDATE CUSTOMER] Request received: {}", dto);
		CreateCustomerResponseDTO response = customerService.updateCustomer(dto);
		log.info("[UPDATE CUSTOMER] Customer updated with ID: {}", response.getId());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/add/store")
	public ResponseEntity<String> addStoreToCustomer(
			@RequestParam Long customerId,
			@RequestParam String storeId
	) {
		String result = customerService.addStoreToCustomer(customerId, storeId);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/my/stores")
	public ResponseEntity<List<StorekeeperEntity>> getMyStores(@RequestParam Long customerId) {
		List<StorekeeperEntity> storekeepers = customerService.getMyStores(customerId);
		return new ResponseEntity<>(storekeepers, HttpStatus.OK);
	}

	@DeleteMapping("/delete/store")
	public ResponseEntity<String> deleteStore(
			@RequestParam Long customerId,
			@RequestParam String storeId
	) {
		String result = customerService.deleteStoreFromCustomer(customerId, storeId);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
