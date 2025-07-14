package com.code.monks.nukkad.services;

import com.code.monks.nukkad.context.UserContextHolder;
import com.code.monks.nukkad.dto.request.CreateAddressRequestDTO;
import com.code.monks.nukkad.dto.request.UpdateAddressRequestDTO;
import com.code.monks.nukkad.dto.response.AddressResponseDTO;
import com.code.monks.nukkad.entities.AddressEntity;
import com.code.monks.nukkad.entities.CustomerEntity;
import com.code.monks.nukkad.exception.AccessDeniedException;
import com.code.monks.nukkad.exception.DefaultAddressUpdateNotAllowedException;
import com.code.monks.nukkad.exception.ResourceNotFoundException;
import com.code.monks.nukkad.exception.UnhandledException;
import com.code.monks.nukkad.repositories.AddressRepository;
import com.code.monks.nukkad.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.code.monks.nukkad.enums.ResponseErrorCodes.*;
@Slf4j
@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;

    public AddressResponseDTO createAddress(CreateAddressRequestDTO request) {
        Long customerId = UserContextHolder.getUser().getId();
        log.info("[CREATE ADDRESS] Creating new address for customerId={}", customerId);

        try {
            CustomerEntity customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> {
                        log.error("[CREATE ADDRESS] Customer not found. ID={}", customerId);
                        return new ResourceNotFoundException(CUSTOMER_NOT_FOUND);
                    });

            AddressEntity address = CreateAddressRequestDTO.toEntity(request);
            address.setCustomerId(customerId);

            // Fill default values if not provided
            if (address.getName() == null || address.getName().isBlank()) {
                address.setName(customer.getName());
            }
            if (address.getMobileNumber() == null || address.getMobileNumber().isBlank()) {
                address.setMobileNumber(customer.getMobileNumber());
            }

            // Save address
            AddressEntity saved = addressRepository.save(address);
            log.info("[CREATE ADDRESS] Address created successfully. addressId={}, customerId={}", saved.getId(), customerId);

            return AddressResponseDTO.fromEntity(saved);

        } catch (ResourceNotFoundException e) {
            throw e;

        } catch (Exception e) {
            log.error("[CREATE ADDRESS] Unexpected error occurred while creating address for customerId={}", customerId, e);
            throw new UnhandledException(UNHANDLED_EXCEPTION, e);
        }
    }


    public AddressResponseDTO updateAddress(Long id, UpdateAddressRequestDTO request) {
        Long customerId = UserContextHolder.getUser().getId();
        log.info("[UPDATE ADDRESS] Attempting to update addressId={} for customerId={}", id, customerId);

        try {
            // Fetch and validate address ownership
            AddressEntity address = addressRepository.findById(id)
                    .filter(a -> a.getCustomerId().equals(customerId))
                    .orElseThrow(() -> {
                        log.warn("[UPDATE ADDRESS] Address not found or does not belong to customer. addressId={}, customerId={}", id, customerId);
                        return new ResourceNotFoundException(ADDRESS_NOT_FOUND);
                    });

            // Check for default address restriction
            if (Boolean.TRUE.equals(address.getIsDefault())) {
                log.warn("[UPDATE ADDRESS] Update not allowed on default address. addressId={}, customerId={}", id, customerId);
                throw new DefaultAddressUpdateNotAllowedException(DEFAULT_ADDRESS_CAN_NOT_BE_CHANGE);
            }

            // Update address fields
            UpdateAddressRequestDTO.updateEntity(address, request);
            AddressEntity updated = addressRepository.save(address);

            log.info("[UPDATE ADDRESS] Address updated successfully. addressId={}, customerId={}", updated.getId(), customerId);
            return AddressResponseDTO.fromEntity(updated);

        } catch (ResourceNotFoundException | DefaultAddressUpdateNotAllowedException e) {
            throw e;

        } catch (Exception e) {
            log.error("[UPDATE ADDRESS] Unexpected error while updating address. addressId={}, customerId={}", id, customerId, e);
            throw new UnhandledException(UNHANDLED_EXCEPTION, e);
        }
    }


    public List<AddressResponseDTO> getAllAddresses() {
        Long customerId = UserContextHolder.getUser().getId();
        log.info("[GET ADDRESSES] Fetching all addresses for customerId={}", customerId);

        try {
            List<AddressResponseDTO> addresses = addressRepository.findByCustomerId(customerId).stream()
                    .map(AddressResponseDTO::fromEntity)
                    .collect(Collectors.toList());

            log.info("[GET ADDRESSES] Found {} address(es) for customerId={}", addresses.size(), customerId);
            return addresses;

        } catch (Exception e) {
            log.error("[GET ADDRESSES] Unexpected error while fetching addresses for customerId={}", customerId, e);
            throw new UnhandledException(UNHANDLED_EXCEPTION, e);
        }
    }


    public void markAsDefault(Long addressId) {
        Long customerId = UserContextHolder.getUser().getId();
        log.info("[MARK DEFAULT] Marking addressId={} as default for customerId={}", addressId, customerId);

        try {
            // Find the address to mark as default
            AddressEntity targetAddress = addressRepository.findByIdAndCustomerId(addressId, customerId)
                    .orElseThrow(() -> {
                        log.warn("[MARK DEFAULT] Address not found or access denied. addressId={}, customerId={}", addressId, customerId);
                        return new ResourceNotFoundException(ADDRESS_NOT_FOUND);
                    });

            // Unset existing default if it's different
            addressRepository.findByCustomerIdAndIsDefaultTrue(customerId).ifPresent(existingDefault -> {
                if (!existingDefault.getId().equals(targetAddress.getId())) {
                    existingDefault.setIsDefault(false);
                    addressRepository.save(existingDefault);
                    log.info("[MARK DEFAULT] Previous default address unset. addressId={}, customerId={}", existingDefault.getId(), customerId);
                }
            });

            // Mark new address as default
            targetAddress.setIsDefault(true);
            addressRepository.save(targetAddress);
            log.info("[MARK DEFAULT] Address marked as default successfully. addressId={}, customerId={}", addressId, customerId);

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("[MARK DEFAULT] Unexpected error while marking address as default. addressId={}, customerId={}", addressId, customerId, e);
            throw new UnhandledException(UNHANDLED_EXCEPTION, e);
        }
    }


    public void deleteAddress(Long addressId) {
        Long customerId = UserContextHolder.getUser().getId();
        log.info("[DELETE ADDRESS] Attempting to delete addressId={} for customerId={}", addressId, customerId);

        try {
            // Check if address exists and belongs to the user
            AddressEntity address = addressRepository.findByIdAndCustomerId(addressId, customerId)
                    .orElseThrow(() -> {
                        log.warn("[DELETE ADDRESS] Address not found or access denied. addressId={}, customerId={}", addressId, customerId);
                        return new ResourceNotFoundException(ADDRESS_NOT_FOUND);
                    });

            // Prevent deletion of default address
            if (Boolean.TRUE.equals(address.getIsDefault())) {
                log.warn("[DELETE ADDRESS] Cannot delete default address. addressId={}, customerId={}", addressId, customerId);
                throw new AccessDeniedException(DEFAULT_ADDRESS_CAN_NOT_BE_DELETE);
            }

            // Proceed with delete
            addressRepository.delete(address);
            log.info("[DELETE ADDRESS] Address deleted successfully. addressId={}, customerId={}", addressId, customerId);

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("[DELETE ADDRESS] Unexpected error occurred while deleting address. addressId={}, customerId={}", addressId, customerId, e);
            throw new UnhandledException(UNHANDLED_EXCEPTION, e);
        }
    }

}
