package com.code.monks.nukkad.services;

import com.code.monks.nukkad.context.UserContextHolder;
import com.code.monks.nukkad.dto.request.CreateCustomerRequestDTO;
import com.code.monks.nukkad.dto.response.CreateCustomerResponseDTO;
import com.code.monks.nukkad.entities.CustomerEntity;
import com.code.monks.nukkad.enums.ResponseErrorCodes;
import com.code.monks.nukkad.exception.AccessDeniedException;
import com.code.monks.nukkad.exception.ResourceNotFoundException;
import com.code.monks.nukkad.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.code.monks.nukkad.enums.RoleEnum;

import static com.code.monks.nukkad.enums.ResponseErrorCodes.ACCESS_DENIED_FOR_STOREKEEPER_EXCEPTION;
import static com.code.monks.nukkad.enums.ResponseErrorCodes.CUSTOMER_NOT_FOUND;


@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

	private final CustomerRepository customerRepository;

	public CreateCustomerResponseDTO createCustomer(CreateCustomerRequestDTO dto) {

		if (!UserContextHolder.getUser().getRoles().contains(RoleEnum.CUSTOMER)) {
			log.warn("[CREATE CUSTOMER] Access denied: User is not a CUSTOMER");
			throw new AccessDeniedException(ACCESS_DENIED_FOR_STOREKEEPER_EXCEPTION);
		}

		Long userId = UserContextHolder.getUser().getId();
		log.info("[CREATE CUSTOMER] Creating customer with ID: {}", userId);

		CustomerEntity customer = CreateCustomerRequestDTO.toEntity(dto);
		customer.setId(userId);

		CustomerEntity saved = customerRepository.save(customer);
		log.info("[CREATE CUSTOMER] Customer created successfully with ID: {}", saved.getId());

		return CreateCustomerResponseDTO.fromEntity(saved);
	}

	public CreateCustomerResponseDTO updateCustomer(CreateCustomerRequestDTO dto) {

		if (!UserContextHolder.getUser().getRoles().contains(RoleEnum.CUSTOMER)) {
			log.warn("[UPDATE CUSTOMER] Access denied: User is not a CUSTOMER");
			throw new AccessDeniedException(ACCESS_DENIED_FOR_STOREKEEPER_EXCEPTION);
		}

		Long userId = UserContextHolder.getUser().getId();
		log.info("[UPDATE CUSTOMER] Updating customer with ID: {}", userId);

		CustomerEntity customer = customerRepository.findById(userId)
				.orElseThrow(() -> {
					log.error("[UPDATE CUSTOMER] Customer not found with ID: {}", userId);
					return new ResourceNotFoundException(CUSTOMER_NOT_FOUND,userId);
				});

		customer.setName(dto.getName());
		customer.setEmail(dto.getEmail());
		customer.setAddressLine1(dto.getAddressLine1());
		customer.setAddressLine2(dto.getAddressLine2());
		customer.setLandmark(dto.getLandmark());
		customer.setCity(dto.getCity());
		customer.setState(dto.getState());
		customer.setPincode(dto.getPincode());
		customer.setDob(dto.getDob());

		CustomerEntity updated = customerRepository.save(customer);
		log.info("[UPDATE CUSTOMER] Customer updated successfully with ID: {}", updated.getId());

		return CreateCustomerResponseDTO.fromEntity(updated);
	}
}
