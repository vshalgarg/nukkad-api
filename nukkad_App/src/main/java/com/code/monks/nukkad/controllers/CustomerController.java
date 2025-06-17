package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.constants.UrlConstants;
import com.code.monks.nukkad.dto.request.CreateCustomerRequestDTO;
import com.code.monks.nukkad.dto.response.AddStoreResponseDto;
import com.code.monks.nukkad.dto.response.CreateCustomerResponseDTO;
import com.code.monks.nukkad.dto.response.DeleteStoreResponseDto;
import com.code.monks.nukkad.dto.response.GetMyStoreResponseDto;
import com.code.monks.nukkad.entities.StorekeeperEntity;
import com.code.monks.nukkad.services.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlConstants.CUSTOMER.BASE)
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

	private final CustomerService customerService;

	@PostMapping(UrlConstants.CUSTOMER.CREATE)
	public ResponseEntity<CreateCustomerResponseDTO> createCustomer(@Valid @RequestBody CreateCustomerRequestDTO dto) {
		log.info("[CREATE CUSTOMER] Request received: {}", dto);
		CreateCustomerResponseDTO response = customerService.createCustomer(dto);
		log.info("[CREATE CUSTOMER] Customer created with ID: {}", response.getId());
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PutMapping(UrlConstants.CUSTOMER.UPDATE)
	public ResponseEntity<CreateCustomerResponseDTO> updateCustomer(@Valid @RequestBody CreateCustomerRequestDTO dto) {
		log.info("[UPDATE CUSTOMER] Request received: {}", dto);
		CreateCustomerResponseDTO response = customerService.updateCustomer(dto);
		log.info("[UPDATE CUSTOMER] Customer updated with ID: {}", response.getId());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/add/store")
	public ResponseEntity<AddStoreResponseDto> addStoreToCustomer(
			@RequestParam Long customerId,
			@RequestParam String storeId
	) {
		AddStoreResponseDto responseDto = customerService.addStoreToCustomer(customerId, storeId);
		return ResponseEntity.ok(responseDto);
	}

	@GetMapping("/get/myStores/{customerId}")
	public ResponseEntity<List<GetMyStoreResponseDto>> getStores(@PathVariable Long customerId) {
		List<GetMyStoreResponseDto> stores = customerService.getMyStores(customerId);
		return ResponseEntity.ok(stores);
	}

	@DeleteMapping("/delete/store")
	public ResponseEntity<DeleteStoreResponseDto> deleteStore(
			@RequestParam Long customerId,
			@RequestParam Long storeKeeperId
	) {
		DeleteStoreResponseDto result = customerService.deleteStoreFromCustomer(customerId, storeKeeperId);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
