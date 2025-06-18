package com.code.monks.nukkad.services;

import com.code.monks.nukkad.context.UserContextHolder;
import com.code.monks.nukkad.dto.request.CreateCustomerRequestDTO;
import com.code.monks.nukkad.dto.response.AddStoreResponseDto;
import com.code.monks.nukkad.dto.response.CreateCustomerResponseDTO;
import com.code.monks.nukkad.entities.AddressEntity;
import com.code.monks.nukkad.dto.response.DeleteStoreResponseDto;
import com.code.monks.nukkad.dto.response.GetMyStoreResponseDto;
import com.code.monks.nukkad.entities.CustomerEntity;
import com.code.monks.nukkad.entities.StorekeeperEntity;
import com.code.monks.nukkad.enums.ResponseErrorCodes;
import com.code.monks.nukkad.exception.AccessDeniedException;
import com.code.monks.nukkad.exception.ResourceNotFoundException;
import com.code.monks.nukkad.repositories.AddressRepository;
import com.code.monks.nukkad.repositories.CustomerRepository;
import com.code.monks.nukkad.repositories.StorekeeperRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.code.monks.nukkad.enums.RoleEnum;
import java.util.List;
import java.util.stream.Collectors;

import static com.code.monks.nukkad.enums.ResponseErrorCodes.ACCESS_DENIED_FOR_STOREKEEPER_EXCEPTION;
import static com.code.monks.nukkad.enums.ResponseErrorCodes.CUSTOMER_NOT_FOUND;


@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

	private final CustomerRepository customerRepository;
	private final AddressRepository addressRepository;
	private final StorekeeperRepository storekeeperRepository;

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

		AddressEntity address = new AddressEntity();
		address.setAddressLine1(dto.getAddressLine1());
		address.setAddressLine2(dto.getAddressLine2());
		address.setLandmark(dto.getLandmark());
		address.setCity(dto.getCity());
		address.setState(dto.getState());
		address.setPincode(dto.getPincode());
		address.setIsDefault(true); // ✅ This is the key
		address.setUserId(customer.getId());
		addressRepository.save(address);

		log.info("[CREATE CUSTOMER] Customer created successfully with ID: {}", saved.getId());

		return CreateCustomerResponseDTO.fromEntity(saved,address);
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
					return new ResourceNotFoundException(CUSTOMER_NOT_FOUND, userId);
				});

		customer.setName(dto.getName());
		customer.setEmail(dto.getEmail());
		customer.setDob(dto.getDob());
		customer.setAddressLine1(dto.getAddressLine1());
		customer.setAddressLine2(dto.getAddressLine2());
		customer.setLandmark(dto.getLandmark());
		customer.setCity(dto.getCity());
		customer.setState(dto.getState());
		customer.setPincode(dto.getPincode());

		CustomerEntity updated = customerRepository.save(customer);

		AddressEntity address = addressRepository.findByUserId(userId)
				.stream().findFirst()
				.orElse(new AddressEntity());

		address.setUserId(userId);
		address.setAddressLine1(dto.getAddressLine1());
		address.setAddressLine2(dto.getAddressLine2());
		address.setLandmark(dto.getLandmark());
		address.setCity(dto.getCity());
		address.setState(dto.getState());
		address.setPincode(dto.getPincode());

		addressRepository.save(address);

		log.info("[UPDATE CUSTOMER] Customer updated successfully with ID: {}", updated.getId());
		return CreateCustomerResponseDTO.fromEntity(updated, address);
	}



	public AddStoreResponseDto addStoreToCustomer(Long customerId, String storeId) {
		CustomerEntity customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new RuntimeException("Customer not found"));

		StorekeeperEntity storekeeper = storekeeperRepository.findByStoreId(storeId)
				.orElseThrow(() -> new RuntimeException(STR."Storekeeper not found with storeId: \{storeId}"));

		if (!customer.getStorekeepers().contains(storekeeper)) {
			customer.getStorekeepers().add(storekeeper);
			customerRepository.save(customer);
			return new AddStoreResponseDto("Store added to customer.");
		} else {
			return new AddStoreResponseDto("Store already added.");
		}
	}


	public List<GetMyStoreResponseDto> getMyStores(Long customerId) {
		CustomerEntity customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new RuntimeException("Customer not found"));
		List<StorekeeperEntity> storekeepers = customer.getStorekeepers();
		return storekeepers.stream()
				.map(storekeeper -> GetMyStoreResponseDto.builder()
						.id(storekeeper.getId())
						.name(storekeeper.getName())
						.storeName(storekeeper.getStoreName())
						.contactNumber(storekeeper.getContactNumber())
						.gstIn(storekeeper.getGstIn())
						.addressLine1(storekeeper.getAddressLine1())
						.addressLine2(storekeeper.getAddressLine2())
						.landmark(storekeeper.getLandmark())
						.city(storekeeper.getCity())
						.state(storekeeper.getState())
						.pincode(storekeeper.getPincode())
						.storeId(storekeeper.getStoreId())
						.build())
				.collect(Collectors.toList());	}

	public DeleteStoreResponseDto deleteStoreFromCustomer(Long customerId, Long storekeeperId) {
		CustomerEntity customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new RuntimeException(STR."Customer not found with ID: \{customerId}"));

		StorekeeperEntity storekeeper = storekeeperRepository.findById(storekeeperId)
				.orElseThrow(() -> new RuntimeException(STR."Storekeeper not found with ID: \{storekeeperId}"));

		if (customer.getStorekeepers().contains(storekeeper)) {
			customer.getStorekeepers().remove(storekeeper);
			customerRepository.save(customer);
			return new DeleteStoreResponseDto("Store removed from customer.");
		} else {
			return new DeleteStoreResponseDto("Store not associated with customer.");
		}
	}
}
