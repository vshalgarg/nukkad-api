package com.code.monks.nukkad.services;

import com.code.monks.nukkad.context.UserContextHolder;
import com.code.monks.nukkad.dto.request.CreateCustomerRequestDTO;
import com.code.monks.nukkad.dto.request.UpdateCustomerRequestDTO;
import com.code.monks.nukkad.dto.response.*;
import com.code.monks.nukkad.entities.AddressEntity;
import com.code.monks.nukkad.entities.CustomerEntity;
import com.code.monks.nukkad.entities.StorekeeperEntity;
import com.code.monks.nukkad.enums.RoleEnum;
import com.code.monks.nukkad.exception.*;
import com.code.monks.nukkad.repositories.AddressRepository;
import com.code.monks.nukkad.repositories.CustomerRepository;
import com.code.monks.nukkad.repositories.StorekeeperRepository;
import com.code.monks.nukkad.utils.FirebaseFileUploadHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import com.code.monks.nukkad.utils.ExceptionHandleUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
	private final FirebaseFileUploadHelper firebaseFileUploadHelper;
	private final ObjectMapper objectMapper;

	public CreateCustomerResponseDTO createCustomer(CreateCustomerRequestDTO dto) {

		log.info("Received create profile req for customer name {}",dto.getName());
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

		//exceptionHandleUtil.validateCustomerUniqueFields(customer);

		try {
			// Save customer

			CustomerEntity saved = customerRepository.save(customer);
			log.info("customer profile saved successfully..............");
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



	public UpdateCustomerResponseDTO updateCustomer(UpdateCustomerRequestDTO  dto) {

		if (!UserContextHolder.getUser().getRoles().contains(RoleEnum.CUSTOMER)) {
			log.warn("[UPDATE CUSTOMER] Access denied. User does not have CUSTOMER role");
			throw new AccessDeniedException(ACCESS_DENIED_FOR_STOREKEEPER_EXCEPTION);
		}


		Long customerId = UserContextHolder.getUser().getId();
		log.info("[UPDATE CUSTOMER] Start updating profile for customerId={}", customerId);

		CustomerEntity customer = customerRepository.findById(customerId)
				.orElseThrow(() -> {
					log.error("[UPDATE CUSTOMER] Customer not found. customerId={}", customerId);
					return new ResourceNotFoundException(CUSTOMER_NOT_FOUND, customerId);
				});

		log.info("Received profile img url for update profile img {}", dto.getProfileImageUrl());
		String previousProfileImageUrl = customer.getProfileImage();
		log.info("old profile img url :{}", customer.getProfileImage());

		customer = UpdateCustomerRequestDTO.updateEntity(customer, dto);

		log.info("[UPDATE CUSTOMER] Validating unique fields (email/mobile) for customerId={}", customerId);
		exceptionHandleUtil.validateCustomerUniqueFields(customer);

		try {
			CustomerEntity updatedCustomer = customerRepository.save(customer);
			log.info("[UPDATE CUSTOMER] Customer profile saved successfully for customerId={}", customerId);
            log.info("updated profile img url :{}", updatedCustomer.getProfileImage());

				// delete old image if exists
				if (dto.getProfileImageUrl() != null && !dto.getProfileImageUrl().isBlank() &&
						previousProfileImageUrl != null && !previousProfileImageUrl.isBlank()) {
					firebaseFileUploadHelper.deleteFile(previousProfileImageUrl);
					log.info("[UPDATE CUSTOMER] Old profile image deleted from Firebase for customerId={}", customerId);
				}

			return UpdateCustomerResponseDTO.fromEntity(updatedCustomer);

		} catch (DataIntegrityViolationException e) {
			log.error("[UPDATE CUSTOMER] Email already exists. Email={}, customerId={}", dto.getEmail(), customerId, e);
			throw new DuplicateResourceException(DUPLICATE_EMAIL_FOUND_EXCEPTION, e);
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

	public GetMyStoreByIdResponseDTO getMyStoreById(String storeQrId) {
		log.info("[GET STORE BY ID] Request received to fetch store with storeQrId={}", storeQrId);

		try {
			StorekeeperEntity storekeeper = storekeeperRepository.findByStoreQrId(storeQrId)
					.orElseThrow(() -> {
						log.error("[GET STORE BY ID] Store not found. storeQrId={}", storeQrId);
						return new ResourceNotFoundException(STORE_NOT_FOUND, storeQrId);
					});

			log.info("[GET STORE BY ID] Store found. storekeeperId={}, storeName={}",
					storekeeper.getId(), storekeeper.getStoreName());

			return GetMyStoreByIdResponseDTO.fromEntity(storekeeper);

		} catch (ResourceNotFoundException e) {
			// Rethrow so controller advice can handle
			throw e;

		} catch (Exception e) {
			log.error("[GET STORE BY ID] Unexpected error occurred while fetching store. storeQrId={}", storeQrId, e);
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

    public SetDefaultStoreResponseDTO setDefaultStore(Long storekeeperId) {
		Long contextUser = UserContextHolder.getUser().getId();
		log.info("Setting default store. customerId={}, storekeeperId={}", contextUser, storekeeperId);

		CustomerEntity currCustomer = customerRepository.findByCustomerId(contextUser);
		StorekeeperEntity storekeeper = storekeeperRepository.findById(storekeeperId)
				.orElseThrow(() -> {
					log.error("[Storekeeper not found. ID={}", storekeeperId);
					return new ResourceNotFoundException(STOREKEEPER_NOT_FOUND, storekeeperId);
				});
		boolean linked = currCustomer.getStorekeepers().stream()
				.anyMatch(s -> s.getId().equals(storekeeperId));
		if (!linked) {
			throw new ResourceNotFoundException(STORE_NOT_LINKED_WITH_CUSTOMER);
		}

		currCustomer.setDefaultStore(storekeeper);
		 customerRepository.save(currCustomer);
		log.info("Default store set successfully for customerId={}, storekeeperId={}", contextUser, storekeeperId);

		return new SetDefaultStoreResponseDTO("Default store set successfully");
	}

	public DefaultStoreResponseDTO getDefaultStore() {
		Long customerId = UserContextHolder.getUser().getId();
		log.info("Fetching default store for customerId={}", customerId);
		CustomerEntity customer = customerRepository.findByCustomerId(customerId);

		StorekeeperEntity store = customer.getDefaultStore();
		if (store == null) {
			throw new ResourceNotFoundException(DEFAULT_STORE_NOT_SET);
		}

		log.info("Default store fetched successfully for customerId={}, storekeeperId={}", customerId, store.getId());
		return new DefaultStoreResponseDTO(
				store.getId(),
				store.getStoreName()
		);
	}
}
