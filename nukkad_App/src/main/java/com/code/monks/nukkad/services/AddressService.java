package com.code.monks.nukkad.services;

import com.code.monks.nukkad.context.UserContextHolder;
import com.code.monks.nukkad.dto.request.CreateAddressRequestDTO;
import com.code.monks.nukkad.dto.request.UpdateAddressRequestDTO;
import com.code.monks.nukkad.dto.response.AddressResponseDTO;
import com.code.monks.nukkad.entities.AddressEntity;
import com.code.monks.nukkad.exception.AccessDeniedException;
import com.code.monks.nukkad.exception.DefaultAddressUpdateNotAllowedException;
import com.code.monks.nukkad.exception.ResourceNotFoundException;
import com.code.monks.nukkad.repositories.AddressRepository;
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

    public AddressResponseDTO createAddress(CreateAddressRequestDTO request) {
        Long userId = UserContextHolder.getUser().getId();
        log.info("Creating address for userId={}", userId);

        AddressEntity address = CreateAddressRequestDTO.toEntity(request);
        address.setUserId(userId);

        AddressEntity saved = addressRepository.save(address);
        log.info("Address created successfully. AddressId={}, userId={}", saved.getId(), userId);

        return AddressResponseDTO.fromEntity(saved);
    }

    public AddressResponseDTO updateAddress(Long id, UpdateAddressRequestDTO request) {
        Long userId = UserContextHolder.getUser().getId();
        log.info("Attempting to update addressId={} for userId={}", id, userId);

        AddressEntity address = addressRepository.findById(id)
                .filter(a -> a.getUserId().equals(userId))
                .orElseThrow(() -> {
                    log.warn("Address not found or access denied. addressId={}, userId={}", id, userId);
                    return new ResourceNotFoundException(ADDRESS_NOT_FOUND);
                });

        if (Boolean.TRUE.equals(address.getIsDefault())) {
            log.warn("Update not allowed on default address. addressId={}, userId={}", id, userId);
            throw new DefaultAddressUpdateNotAllowedException(DEFAULT_ADDRESS_CAN_NOT_BE_CHANGE);
        }

        UpdateAddressRequestDTO.updateEntity(address, request);
        AddressEntity updated = addressRepository.save(address);
        log.info("Address updated successfully. addressId={}, userId={}", updated.getId(), userId);

        return AddressResponseDTO.fromEntity(updated);
    }

    public List<AddressResponseDTO> getAllAddresses() {
        Long userId = UserContextHolder.getUser().getId();
        log.info("Fetching all addresses for userId={}", userId);

        List<AddressResponseDTO> addresses = addressRepository.findByUserId(userId).stream()
                .map(AddressResponseDTO::fromEntity)
                .collect(Collectors.toList());

        log.info("Found {} address(es) for userId={}", addresses.size(), userId);
        return addresses;
    }

    public void markAsDefault(Long addressId) {
        Long userId = UserContextHolder.getUser().getId();
        log.info("Marking addressId={} as default for userId={}", addressId, userId);

        AddressEntity targetAddress = addressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> {
                    log.warn("Address not found or access denied during markAsDefault. addressId={}, userId={}", addressId, userId);
                    return new ResourceNotFoundException(ADDRESS_NOT_FOUND);
                });

        addressRepository.findByUserIdAndIsDefaultTrue(userId).ifPresent(existingDefault -> {
            if (!existingDefault.getId().equals(targetAddress.getId())) {
                existingDefault.setIsDefault(false);
                addressRepository.save(existingDefault);
                log.info("Previous default address unset. addressId={}, userId={}", existingDefault.getId(), userId);
            }
        });

        targetAddress.setIsDefault(true);
        addressRepository.save(targetAddress);
        log.info("Address marked as default successfully. addressId={}, userId={}", addressId, userId);
    }

    public void deleteAddress(Long addressId) {
        Long userId = UserContextHolder.getUser().getId();
        log.info("Attempting to delete addressId={} for userId={}", addressId, userId);

        AddressEntity address = addressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> {
                    log.warn("Address not found or access denied during delete. addressId={}, userId={}", addressId, userId);
                    return new ResourceNotFoundException(ADDRESS_NOT_FOUND);
                });

        if (Boolean.TRUE.equals(address.getIsDefault())) {
            log.warn("Cannot delete default address. addressId={}, userId={}", addressId, userId);
            throw new AccessDeniedException(DEFAULT_ADDRESS_CAN_NOT_BE_DELETE);
        }

        addressRepository.delete(address);
        log.info("Address deleted successfully. addressId={}, userId={}", addressId, userId);
    }
}
