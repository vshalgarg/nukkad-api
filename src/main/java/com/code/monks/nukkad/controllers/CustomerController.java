package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.dto.request.CreateCustomerRequestDTO;
import com.code.monks.nukkad.dto.request.UpdateCustomerRequestDTO;
import com.code.monks.nukkad.dto.response.*;
import com.code.monks.nukkad.services.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import static com.code.monks.nukkad.constants.UrlConstants.CUSTOMER;

@RestController
@RequestMapping(CUSTOMER.BASE)
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

	private final CustomerService customerService;

	@PostMapping(CUSTOMER.CREATE)
	public ResponseEntity<CreateCustomerResponseDTO> createCustomer(@Valid @RequestBody CreateCustomerRequestDTO dto) {
		log.info("[CREATE CUSTOMER] Incoming request to create customer: {}", dto);

		CreateCustomerResponseDTO response = customerService.createCustomer(dto);

		log.info("[CREATE CUSTOMER] Customer successfully created with ID={}", response.getId());
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}


	@PutMapping(value = CUSTOMER.UPDATE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<UpdateCustomerResponseDTO> updateCustomer(
			@RequestPart("data") @Valid UpdateCustomerRequestDTO dto,
			@RequestPart(value = "image", required = false) MultipartFile image) {

		log.info("[UPDATE CUSTOMER] Incoming update request for customer: {}", dto);

		UpdateCustomerResponseDTO response = customerService.updateCustomer(dto, image);

		log.info("[UPDATE CUSTOMER] Customer successfully updated. ID={}", response.getId());
		return ResponseEntity.ok(response);
	}

	@GetMapping(CUSTOMER.GET_PROFILE)
	public ResponseEntity<GetCustomerProfileResponseDTO> getCustomerProfile() {
		log.info("[GET PROFILE] Request to fetch customer profile");

		GetCustomerProfileResponseDTO response = customerService.getCustomerProfile();

		log.info("[GET PROFILE] Profile fetched for customerId={}", response.getId());
		return ResponseEntity.ok(response);
	}


	@PostMapping(CUSTOMER.ADD_STORE_TO_CUSTOMER)
	public ResponseEntity<AddStoreResponseDto> addStoreToCustomer(@RequestParam String storeQrId) {
		log.info("[ADD STORE] Adding store with QR ID={} to customer", storeQrId);

		AddStoreResponseDto responseDto = customerService.addStoreToCustomer(storeQrId);

		log.info("[ADD STORE] {}", responseDto.getMessage());
		return ResponseEntity.ok(responseDto);
	}


	@GetMapping(CUSTOMER.GET_MY_STORES)
	public ResponseEntity<List<GetMyStoreResponseDto>> getStores() {
		log.info("[GET STORES] Request to fetch stores linked to customer");

		List<GetMyStoreResponseDto> stores = customerService.getMyStores();

		log.info("[GET STORES] {} store(s) fetched successfully", stores.size());
		return ResponseEntity.ok(stores);
	}


	@GetMapping(CUSTOMER.GET_MY_STORE_BY_STORE_QR_ID)
	public ResponseEntity<GetMyStoreByIdResponseDTO> getMyStoreById(@RequestParam String storeQrId) {
		log.info("[GET STORE BY ID API] Request received. storeQrId={}", storeQrId);

		GetMyStoreByIdResponseDTO responseDto = customerService.getMyStoreById(storeQrId);

		log.info("[GET STORE BY ID API] Response prepared");
		return ResponseEntity.ok(responseDto);
	}


	@DeleteMapping(CUSTOMER.DELETE_STORE)
	public ResponseEntity<DeleteStoreResponseDto> deleteStore(@RequestParam Long storekeeperId) {
		log.info("[DELETE STORE] Request to delete storekeeperId={} from customer", storekeeperId);

		DeleteStoreResponseDto result = customerService.deleteStoreFromCustomer(storekeeperId);

		log.info("[DELETE STORE] {}", result.getMessage());
		return ResponseEntity.ok(result);
	}


}
