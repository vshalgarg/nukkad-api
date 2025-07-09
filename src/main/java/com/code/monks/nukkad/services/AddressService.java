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
        log.info("Creating address for userId={}", customerId);

        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    log.error("Customer not found while creating address. userId={}", customerId);
                    return new ResourceNotFoundException(CUSTOMER_NOT_FOUND);
                });

        AddressEntity address = CreateAddressRequestDTO.toEntity(request);
        address.setCustomerId(customerId);

        if (address.getName() == null || address.getName().isBlank()) {
            address.setName(customer.getName());
        }
        if (address.getMobileNumber() == null || address.getMobileNumber().isBlank()) {
            address.setMobileNumber(customer.getMobileNumber());
        }

        AddressEntity saved = addressRepository.save(address);
        log.info("Address created successfully. AddressId={}, userId={}", saved.getId(), customerId);

        return AddressResponseDTO.fromEntity(saved);
    }

    public AddressResponseDTO updateAddress(Long id, UpdateAddressRequestDTO request) {
        Long customerId = UserContextHolder.getUser().getId();
        log.info("Attempting to update addressId={} for userId={}", id, customerId);

        AddressEntity address = addressRepository.findById(id)
                .filter(a -> a.getCustomerId().equals(customerId))
                .orElseThrow(() -> {
                    log.warn("Address not found or access denied. addressId={}, userId={}", id, customerId);
                    return new ResourceNotFoundException(ADDRESS_NOT_FOUND);
                });

        if (Boolean.TRUE.equals(address.getIsDefault())) {
            log.warn("Update not allowed on default address. addressId={}, userId={}", id, customerId);
            throw new DefaultAddressUpdateNotAllowedException(DEFAULT_ADDRESS_CAN_NOT_BE_CHANGE);
        }

        UpdateAddressRequestDTO.updateEntity(address, request);
        AddressEntity updated = addressRepository.save(address);
        log.info("Address updated successfully. addressId={}, userId={}", updated.getId(), customerId);

        return AddressResponseDTO.fromEntity(updated);
    }

    public List<AddressResponseDTO> getAllAddresses() {
        Long customerId = UserContextHolder.getUser().getId();
        log.info("Fetching all addresses for userId={}", customerId);

        List<AddressResponseDTO> addresses = addressRepository.findByCustomerId(customerId).stream()
                .map(AddressResponseDTO::fromEntity)
                .collect(Collectors.toList());

        log.info("Found {} address(es) for userId={}", addresses.size(), customerId);
        return addresses;
    }

    public void markAsDefault(Long addressId) {
        Long customerId = UserContextHolder.getUser().getId();
        log.info("Marking addressId={} as default for userId={}", addressId, customerId);

        AddressEntity targetAddress = addressRepository.findByIdAndCustomerId(addressId, customerId)
                .orElseThrow(() -> {
                    log.warn("Address not found or access denied during markAsDefault. addressId={}, userId={}", addressId, customerId);
                    return new ResourceNotFoundException(ADDRESS_NOT_FOUND);
                });

        addressRepository.findByCustomerIdAndIsDefaultTrue(customerId).ifPresent(existingDefault -> {
            if (!existingDefault.getId().equals(targetAddress.getId())) {
                existingDefault.setIsDefault(false);
                addressRepository.save(existingDefault);
                log.info("Previous default address unset. addressId={}, userId={}", existingDefault.getId(), customerId);
            }
        });

        targetAddress.setIsDefault(true);
        addressRepository.save(targetAddress);
        log.info("Address marked as default successfully. addressId={}, userId={}", addressId, customerId);
    }

    public void deleteAddress(Long addressId) {
        Long customerId = UserContextHolder.getUser().getId();
        log.info("Attempting to delete addressId={} for userId={}", addressId, customerId);

        AddressEntity address = addressRepository.findByIdAndCustomerId(addressId, customerId)
                .orElseThrow(() -> {
                    log.warn("Address not found or access denied during delete. addressId={}, userId={}", addressId, customerId);
                    return new ResourceNotFoundException(ADDRESS_NOT_FOUND);
                });

        if (Boolean.TRUE.equals(address.getIsDefault())) {
            log.warn("Cannot delete default address. addressId={}, userId={}", addressId, customerId);
            throw new AccessDeniedException(DEFAULT_ADDRESS_CAN_NOT_BE_DELETE);
        }

        addressRepository.delete(address);
        log.info("Address deleted successfully. addressId={}, userId={}", addressId, customerId);
    }
}
