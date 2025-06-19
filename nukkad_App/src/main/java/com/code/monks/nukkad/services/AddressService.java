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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.code.monks.nukkad.enums.ResponseErrorCodes.*;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressResponseDTO createAddress(CreateAddressRequestDTO request) {
        Long customerId = UserContextHolder.getUser().getId();

        AddressEntity address = CreateAddressRequestDTO.toEntity(request);
        address.setUserId(customerId);

        AddressEntity saved = addressRepository.save(address);
        return AddressResponseDTO.fromEntity(saved);
    }

    public AddressResponseDTO updateAddress(Long id, UpdateAddressRequestDTO request) {
        Long customerId = UserContextHolder.getUser().getId();

        AddressEntity address = addressRepository.findById(id)
                .filter(a -> a.getUserId().equals(customerId))
                .orElseThrow(() -> new ResourceNotFoundException(ITEM_NOT_FOUND));

        if (Boolean.TRUE.equals(address.getIsDefault())) {
            throw new DefaultAddressUpdateNotAllowedException(DEFAULT_ADDRESS_CAN_NOT_BE_CHANGE);
        }

        UpdateAddressRequestDTO.updateEntity(address, request);
        AddressEntity updated = addressRepository.save(address);
        return AddressResponseDTO.fromEntity(updated);
    }

    public List<AddressResponseDTO> getAllAddresses() {
        Long customerId = UserContextHolder.getUser().getId();

        return addressRepository.findByUserId(customerId).stream()
                .map(AddressResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public void markAsDefault(Long addressId) {
        Long customerId = UserContextHolder.getUser().getId();

        AddressEntity targetAddress = addressRepository.findByIdAndUserId(addressId, customerId)
                .orElseThrow(() -> new ResourceNotFoundException(ADDRESS_NOT_FOUND, addressId));

        addressRepository.findByUserIdAndIsDefaultTrue(customerId).ifPresent(existingDefault -> {
            if (!existingDefault.getId().equals(targetAddress.getId())) {
                existingDefault.setIsDefault(false);
                addressRepository.save(existingDefault);
            }
        });

        targetAddress.setIsDefault(true);
        addressRepository.save(targetAddress);
    }

    public void deleteAddress(Long addressId) {
        Long customerId = UserContextHolder.getUser().getId();

        AddressEntity address = addressRepository.findByIdAndUserId(addressId, customerId)
                .orElseThrow(() -> new ResourceNotFoundException(ADDRESS_NOT_FOUND, addressId));

        if (Boolean.TRUE.equals(address.getIsDefault())) {
            throw new AccessDeniedException(DEFAULT_ADDRESS_CAN_NOT_BE_CHANGE);
        }

        addressRepository.delete(address);
    }
}
