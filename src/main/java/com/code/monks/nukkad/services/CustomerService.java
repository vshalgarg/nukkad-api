package com.code.monks.nukkad.services;

import com.code.monks.nukkad.context.UserContextHolder;
import com.code.monks.nukkad.dto.request.CreateCustomerRequestDTO;
import com.code.monks.nukkad.dto.request.UpdateCustomerRequestDTO;
import com.code.monks.nukkad.dto.response.*;
import com.code.monks.nukkad.entities.AddressEntity;
import com.code.monks.nukkad.entities.CustomerEntity;
import com.code.monks.nukkad.entities.StorekeeperEntity;
import com.code.monks.nukkad.enums.RoleEnum;
import com.code.monks.nukkad.exception.AccessDeniedException;
import com.code.monks.nukkad.exception.ResourceNotFoundException;
import com.code.monks.nukkad.repositories.AddressRepository;
import com.code.monks.nukkad.repositories.CustomerRepository;
import com.code.monks.nukkad.repositories.StorekeeperRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import static com.code.monks.nukkad.dto.response.CreateCustomerResponseDTO.setAddress;
import static com.code.monks.nukkad.enums.ResponseErrorCodes.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

	private final CustomerRepository customerRepository;
	private final AddressRepository addressRepository;
	private final StorekeeperRepository storekeeperRepository;

	public CreateCustomerResponseDTO createCustomer(CreateCustomerRequestDTO dto) {
		if (!UserContextHolder.getUser().getRoles().contains(RoleEnum.CUSTOMER)) {
			log.warn("[CREATE CUSTOMER] Access denied: User role does not include CUSTOMER");
			throw new AccessDeniedException(ACCESS_DENIED_FOR_STOREKEEPER_EXCEPTION);
		}

		Long customerId = UserContextHolder.getUser().getId();
		String mobileNumber = UserContextHolder.getUser().getMobileNumber();
		log.info("[CREATE CUSTOMER] Creating new customer profile for customerId={} and mobileNumber={}", customerId,mobileNumber);

		CustomerEntity customer = CreateCustomerRequestDTO.toEntity(dto);
		customer.setId(customerId);
		customer.setMobileNumber(mobileNumber);
		CustomerEntity saved = customerRepository.save(customer);

		log.info("[CREATE CUSTOMER] Saving default address for customerId={}", saved.getId());

		AddressEntity address = setAddress(dto, saved.getId(),saved.getName(),saved.getMobileNumber());
		addressRepository.save(address);
		log.info("[CREATE CUSTOMER] Customer and address created successfully for customerId={}", saved.getId());

		return CreateCustomerResponseDTO.fromEntity(saved);
	}

	public UpdateCustomerResponseDTO updateCustomer(UpdateCustomerRequestDTO dto) {
		if (!UserContextHolder.getUser().getRoles().contains(RoleEnum.CUSTOMER)) {
			log.warn("[UPDATE CUSTOMER] Access denied: User role does not include CUSTOMER");
			throw new AccessDeniedException(ACCESS_DENIED_FOR_STOREKEEPER_EXCEPTION);
		}

		Long customerId = UserContextHolder.getUser().getId();
		log.info("[UPDATE CUSTOMER] Updating customer profile for customerId={}", customerId);

		CustomerEntity customer = customerRepository.findById(customerId)
				.orElseThrow(() -> {
					log.error("[UPDATE CUSTOMER] Customer not found. ID={}", customerId);
					return new ResourceNotFoundException(CUSTOMER_NOT_FOUND, customerId);
				});

		customer = UpdateCustomerRequestDTO.updateEntity(customer, dto);
		CustomerEntity updated = customerRepository.save(customer);
		log.info("[UPDATE CUSTOMER] Customer profile updated. customerId={}", updated.getId());

		return UpdateCustomerResponseDTO.fromEntity(updated);
	}

	public AddStoreResponseDto addStoreToCustomer(String storeQrId) {
		Long customerId = UserContextHolder.getUser().getId();
		log.info("[ADD STORE] Adding store with QR ID={} to customerId={}", storeQrId, customerId);

		CustomerEntity customer = customerRepository.findById(customerId)
				.orElseThrow(() -> {
					log.error("[ADD STORE] Customer not found. ID={}", customerId);
					return new ResourceNotFoundException(CUSTOMER_NOT_FOUND, customerId);
				});

		StorekeeperEntity storekeeper = storekeeperRepository.findByStoreQrId(storeQrId)
				.orElseThrow(() -> {
					log.error("[ADD STORE] Storekeeper not found with QR ID={}", storeQrId);
					return new ResourceNotFoundException(STOREKEEPER_NOT_FOUND, storeQrId);
				});

		if (!customer.getStorekeepers().contains(storekeeper)) {
			customer.getStorekeepers().add(storekeeper);
			customerRepository.save(customer);
			log.info("[ADD STORE] Storekeeper successfully linked to customerId={}", customerId);
			return new AddStoreResponseDto("Store added to customer.");
		} else {
			log.info("[ADD STORE] Storekeeper already linked to customerId={}", customerId);
			return new AddStoreResponseDto("Store already added.");
		}
	}



	public List<GetMyStoreResponseDto> getMyStores() {
		Long customerId = UserContextHolder.getUser().getId();
		log.info("[GET STORES] Fetching all linked stores for customerId={}", customerId);

		CustomerEntity customer = customerRepository.findById(customerId)
				.orElseThrow(() -> {
					log.error("[GET STORES] Customer not found. ID={}", customerId);
                    return new ResourceNotFoundException(CUSTOMER_NOT_FOUND, customerId);
				});

		List<StorekeeperEntity> storekeepers = customer.getStorekeepers();
		log.info("[GET STORES] {} stores found for customerId={}", storekeepers.size(), customerId);

		return storekeepers.stream()
				.map(storekeeper -> GetMyStoreResponseDto.builder()
						.id(storekeeper.getId())
						.name(storekeeper.getName())
						.storeName(storekeeper.getStoreName())
						.mobileNumber(storekeeper.getMobileNumber())
						.gstIn(storekeeper.getGstIn()) 
						.addressLine1(storekeeper.getAddressLine1())
						.addressLine2(storekeeper.getAddressLine2())
						.landmark(storekeeper.getLandmark())
						.city(storekeeper.getCity())
						.state(storekeeper.getState())
						.pincode(storekeeper.getPincode())
						.storeId(storekeeper.getStoreQrId())
						.build())
				.collect(Collectors.toList());
	}

	public DeleteStoreResponseDto deleteStoreFromCustomer(Long storekeeperId) {
		Long customerId = UserContextHolder.getUser().getId();
		log.info("[DELETE STORE] Attempting to unlink storekeeperId={} from customerId={}", storekeeperId, customerId);

		CustomerEntity customer = customerRepository.findById(customerId)
				.orElseThrow(() -> {
					log.error("[DELETE STORE] Customer not found. ID={}", customerId);
                    return new ResourceNotFoundException(CUSTOMER_NOT_FOUND, customerId);
				});

		StorekeeperEntity storekeeper = storekeeperRepository.findById(storekeeperId)
				.orElseThrow(() -> {
					log.error("[DELETE STORE] Storekeeper not found. ID={}", storekeeperId);
                    return new ResourceNotFoundException(STOREKEEPER_NOT_FOUND, storekeeperId);
				});

		if (customer.getStorekeepers().contains(storekeeper)) {
			customer.getStorekeepers().remove(storekeeper);
			customerRepository.save(customer);
			log.info("[DELETE STORE] Store successfully removed from customerId={}", customerId);
			return new DeleteStoreResponseDto("Store removed from customer.");
		} else {
			log.warn("[DELETE STORE] Store was not linked to customerId={}", customerId);
			return new DeleteStoreResponseDto("Store not associated with customer.");
		}
	}


	public GetCustomerProfileResponseDTO getCustomerProfile() {
		if (!UserContextHolder.getUser().getRoles().contains(RoleEnum.CUSTOMER)) {
			log.warn("[GET PROFILE] Access denied: User role does not include CUSTOMER");
			throw new AccessDeniedException(ACCESS_DENIED_FOR_STOREKEEPER_EXCEPTION);
		}

		Long customerId = UserContextHolder.getUser().getId();
		log.info("[GET PROFILE] Fetching profile for customerId={}", customerId);

		CustomerEntity customer = customerRepository.findById(customerId)
				.orElseThrow(() -> {
					log.error("[GET PROFILE] Customer not found. ID={}", customerId);
					return new ResourceNotFoundException(CUSTOMER_NOT_FOUND, customerId);
				});

		return GetCustomerProfileResponseDTO.fromEntity(customer);
	}

}
