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
import com.code.monks.nukkad.exception.DuplicateResourceException;
import com.code.monks.nukkad.exception.ResourceNotFoundException;
import com.code.monks.nukkad.exception.UnhandledException;
import com.code.monks.nukkad.repositories.AddressRepository;
import com.code.monks.nukkad.repositories.CustomerRepository;
import com.code.monks.nukkad.repositories.StorekeeperRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import com.code.monks.nukkad.utils.ExceptionHandleUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
	private final ExceptionHandleUtil exceptionHandleUtil;
	private final NotificationStatusService notificationStatusService;

	public CreateCustomerResponseDTO createCustomer(CreateCustomerRequestDTO dto) {
		if (!UserContextHolder.getUser().getRoles().contains(RoleEnum.CUSTOMER)) {
			log.warn("[CREATE CUSTOMER] Access denied: User role does not include CUSTOMER");
			throw new AccessDeniedException(ACCESS_DENIED_FOR_STOREKEEPER_EXCEPTION);
		}

		Long customerId = UserContextHolder.getUser().getId();
		String mobileNumber = UserContextHolder.getUser().getMobileNumber();
		log.info("[CREATE CUSTOMER] Creating new customer profile for customerId={} and mobileNumber={}", customerId, mobileNumber);

		// Check if customer already exists
		if (customerRepository.existsById(customerId)) {
			log.error("[CREATE CUSTOMER] Customer already exists for customerId={}", customerId);
			throw new DuplicateResourceException(DUPLICATE_CUSTOMER_PROFILE_FOUND_EXCEPTION);
		}

		CustomerEntity customer = CreateCustomerRequestDTO.toEntity(dto);
		customer.setId(customerId);
		customer.setMobileNumber(mobileNumber);

		exceptionHandleUtil.validateCustomerUniqueFields(customer);

		try {
			// Save customer
			CustomerEntity saved = customerRepository.save(customer);

			// Set default notification status ON
			notificationStatusService.initializeStatusIfAbsent();

			AddressEntity savedAddress = null;

			// Only save address if addressLine1 is provided
			boolean isAddressProvided = dto.getAddressLine1() != null && !dto.getAddressLine1().isBlank();

			if (isAddressProvided) {
				log.info("[CREATE CUSTOMER] Saving address for customerId={}", saved.getId());
				AddressEntity address = setAddress(dto, saved.getId(), saved.getName(), saved.getMobileNumber());
				address.setIsDefault(true); // mark as default
				savedAddress = addressRepository.save(address);
			} else {
				log.info("[CREATE CUSTOMER] No valid address (addressLine1 missing) for customerId={}, skipping address save", saved.getId());
			}

			log.info("[CREATE CUSTOMER] Customer created successfully for customerId={}", saved.getId());

			// Prepare response
			CreateCustomerResponseDTO responseDTO = CreateCustomerResponseDTO.fromEntity(saved);
			if (savedAddress != null) {
				responseDTO.setAddressId(savedAddress.getId());
			}
			return responseDTO;

		} catch (Exception e) {
			log.error("[CREATE CUSTOMER] Data integrity violation while creating customer", e);
			throw new UnhandledException(UNHANDLED_EXCEPTION, e);
		}
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

		exceptionHandleUtil.validateCustomerUniqueFields(customer);

		try {
			// Save customer
			CustomerEntity updated = customerRepository.save(customer);
			log.info("[UPDATE CUSTOMER] Customer profile updated. customerId={}", updated.getId());

			// Update default address if exists
			Optional<AddressEntity> defaultAddressOpt = addressRepository.findByCustomerIdAndIsDefaultTrue(customerId);
			if (defaultAddressOpt.isPresent()) {
				AddressEntity defaultAddress = defaultAddressOpt.get();
				defaultAddress.setName(updated.getName());
				defaultAddress.setMobileNumber(updated.getMobileNumber());
				addressRepository.save(defaultAddress);
				log.info("[UPDATE CUSTOMER] Default address updated with new name and mobile number for customerId={}", customerId);
			} else {
				log.warn("[UPDATE CUSTOMER] Default address not found for customerId={}", customerId);
			}

			// Return response
			return UpdateCustomerResponseDTO.fromEntity(updated);

		} catch (DataIntegrityViolationException e) {
			log.error("[UPDATE CUSTOMER] Email already exists. Email={}, customerId={}", dto.getEmail(), customerId, e);
			throw new DuplicateResourceException(DUPLICATE_EMAIL_FOUND_EXCEPTION,e);
		}
	}


	public AddStoreResponseDto addStoreToCustomer(String storeQrId) {
		Long customerId = UserContextHolder.getUser().getId();
		log.info("[ADD STORE] Request received to add store with QR ID={} for customerId={}", storeQrId, customerId);

		try {
			// Fetch Customer
			CustomerEntity customer = customerRepository.findById(customerId)
					.orElseThrow(() -> {
						log.error("[ADD STORE] Customer not found. ID={}", customerId);
						return new ResourceNotFoundException(CUSTOMER_NOT_FOUND, customerId);
					});

			// Fetch Storekeeper
			StorekeeperEntity storekeeper = storekeeperRepository.findByStoreQrId(storeQrId)
					.orElseThrow(() -> {
						log.error("[ADD STORE] Storekeeper not found with QR ID={}", storeQrId);
						return new ResourceNotFoundException(STOREKEEPER_NOT_FOUND, storeQrId);
					});

			// Check if already linked
			if (!customer.getStorekeepers().contains(storekeeper)) {
				customer.getStorekeepers().add(storekeeper);
				customerRepository.save(customer);
				log.info("[ADD STORE] Storekeeper successfully linked. customerId={}, storekeeperId={}", customerId, storekeeper.getId());
				return new AddStoreResponseDto(
						storekeeper.getId(),
						storekeeper.getName(),
						storekeeper.getStoreName(),
						storekeeper.getAddressLine1(),
						storekeeper.getAddressLine2(),
						storekeeper.getStoreQrId(),
						"Store added to customer.");
			} else {
				log.info("[ADD STORE] Storekeeper already linked. customerId={}, storekeeperId={}", customerId, storekeeper.getId());
				return new AddStoreResponseDto(
						storekeeper.getId(),
						storekeeper.getName(),
						storekeeper.getStoreName(),
						storekeeper.getAddressLine1(),
						storekeeper.getAddressLine2(),
						storekeeper.getStoreQrId(),
						"Store already added.");
			}

		} catch (ResourceNotFoundException e) {

			throw e;
		} catch (Exception e) {
			log.error("[ADD STORE] Unexpected error occurred while linking store. QR ID={}, customerId={}", storeQrId, customerId, e);
			throw new UnhandledException(UNHANDLED_EXCEPTION, e);
		}
	}



	public List<GetMyStoreResponseDto> getMyStores() {
		Long customerId = UserContextHolder.getUser().getId();
		log.info("[GET STORES] Request received to fetch linked stores for customerId={}", customerId);

		try {
			// Fetch customer
			CustomerEntity customer = customerRepository.findById(customerId)
					.orElseThrow(() -> {
						log.error("[GET STORES] Customer not found. ID={}", customerId);
						return new ResourceNotFoundException(CUSTOMER_NOT_FOUND, customerId);
					});

			// Fetch linked storekeepers
			List<StorekeeperEntity> storekeepers = customer.getStorekeepers();
			log.info("[GET STORES] {} store(s) found for customerId={}", storekeepers.size(), customerId);

			// Map to response DTO
			return storekeepers.stream()
					.map(storekeeper -> GetMyStoreResponseDto.builder()
							.id(storekeeper.getId())
							.name(storekeeper.getName())
							.storeName(storekeeper.getStoreName())
							.mobileNumber(storekeeper.getMobileNumber())
							.gstNum(storekeeper.getGstNum())
							.addressLine1(storekeeper.getAddressLine1())
							.addressLine2(storekeeper.getAddressLine2())
							.landmark(storekeeper.getLandmark())
							.city(storekeeper.getCity())
							.state(storekeeper.getState())
							.pincode(storekeeper.getPincode())
							.storeId(storekeeper.getStoreQrId())
							.build())
					.collect(Collectors.toList());

		} catch (ResourceNotFoundException e) {
			throw e;

		} catch (Exception e) {
			log.error("[GET STORES] Unexpected error occurred while fetching stores for customerId={}", customerId, e);
			throw new UnhandledException(UNHANDLED_EXCEPTION, e);
		}
	}




	public DeleteStoreResponseDto deleteStoreFromCustomer(Long storekeeperId) {
		Long customerId = UserContextHolder.getUser().getId();
		log.info("[DELETE STORE] Request to unlink storekeeperId={} from customerId={}", storekeeperId, customerId);

		try {
			// Validate customer
			CustomerEntity customer = customerRepository.findById(customerId)
					.orElseThrow(() -> {
						log.error("[DELETE STORE] Customer not found. ID={}", customerId);
						return new ResourceNotFoundException(CUSTOMER_NOT_FOUND, customerId);
					});

			// Validate storekeeper
			StorekeeperEntity storekeeper = storekeeperRepository.findById(storekeeperId)
					.orElseThrow(() -> {
						log.error("[DELETE STORE] Storekeeper not found. ID={}", storekeeperId);
						return new ResourceNotFoundException(STOREKEEPER_NOT_FOUND, storekeeperId);
					});

			// Remove if linked
			if (customer.getStorekeepers().contains(storekeeper)) {
				customer.getStorekeepers().remove(storekeeper);
				customerRepository.save(customer);
				log.info("[DELETE STORE] Storekeeper unlinked successfully from customerId={}", customerId);
				return new DeleteStoreResponseDto("Store removed from customer.");
			} else {
				log.warn("[DELETE STORE] Storekeeper not associated with customerId={}", customerId);
				return new DeleteStoreResponseDto("Store not associated with customer.");
			}

		} catch (ResourceNotFoundException e) {
			throw e;

		} catch (Exception e) {
			log.error("[DELETE STORE] Unexpected error while unlinking storekeeperId={} from customerId={}", storekeeperId, customerId, e);
			throw new UnhandledException(UNHANDLED_EXCEPTION, e);
		}
	}



	public GetCustomerProfileResponseDTO getCustomerProfile() {
		if (!UserContextHolder.getUser().getRoles().contains(RoleEnum.CUSTOMER)) {
			log.warn("[GET PROFILE] Access denied: User role does not include CUSTOMER");
			throw new AccessDeniedException(ACCESS_DENIED_FOR_STOREKEEPER_EXCEPTION);
		}

		Long customerId = UserContextHolder.getUser().getId();
		log.info("[GET PROFILE] Fetching profile for customerId={}", customerId);

		try {
			CustomerEntity customer = customerRepository.findById(customerId)
					.orElseThrow(() -> {
						log.error("[GET PROFILE] Customer not found. ID={}", customerId);
						return new ResourceNotFoundException(CUSTOMER_NOT_FOUND, customerId);
					});

			log.info("[GET PROFILE] Profile fetched successfully for customerId={}", customerId);
			return GetCustomerProfileResponseDTO.fromEntity(customer);

		} catch (ResourceNotFoundException e) {
			throw e;

		} catch (Exception e) {
			log.error("[GET PROFILE] Unexpected error occurred while fetching profile for customerId={}", customerId, e);
			throw new UnhandledException(UNHANDLED_EXCEPTION, e);
		}
	}


}
