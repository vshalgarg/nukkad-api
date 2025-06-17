package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.constants.UrlConstants;
import com.code.monks.nukkad.dto.request.CreateCustomerRequestDTO;
import com.code.monks.nukkad.dto.response.CreateCustomerResponseDTO;
import com.code.monks.nukkad.entities.CustomerEntity;
import com.code.monks.nukkad.entities.StorekeeperEntity;
import com.code.monks.nukkad.services.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

			@Valid @RequestBody CreateCustomerRequestDTO dto) {
		CreateCustomerResponseDTO updateCustomer = customerService.updateCustomer(dto);
		return new ResponseEntity<>(updateCustomer,HttpStatus.OK);
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
