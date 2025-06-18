package com.code.monks.nukkad.services;

import com.code.monks.nukkad.context.UserContextHolder;
import com.code.monks.nukkad.dto.request.CreateAddressRequestDTO;
import com.code.monks.nukkad.dto.request.UpdateAddressRequestDTO;
import com.code.monks.nukkad.dto.response.AddressResponseDTO;
import com.code.monks.nukkad.entities.AddressEntity;
import com.code.monks.nukkad.enums.ResponseErrorCodes;
import com.code.monks.nukkad.exception.AccessDeniedException;
import com.code.monks.nukkad.exception.DefaultAddressUpdateNotAllowedException;
import com.code.monks.nukkad.exception.ResourceNotFoundException;
import com.code.monks.nukkad.repositories.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.code.monks.nukkad.enums.ResponseErrorCodes.DEFAULT_ADDRESS_CAN_NOT_BE_CHANGE;


@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

     public AddressResponseDTO createAddress(CreateAddressRequestDTO request) {
        var context = UserContextHolder.getRequiredUser();

        AddressEntity address = CreateAddressRequestDTO.toEntity(request);
        address.setUserId(context.getId());



        AddressEntity saved = addressRepository.save(address);
        return AddressResponseDTO.fromEntity(saved);
    }

    public AddressResponseDTO updateAddress(Long id, UpdateAddressRequestDTO request) {
        var context = UserContextHolder.getRequiredUser();

        AddressEntity address = addressRepository.findById(id)
                .filter(a -> a.getUserId().equals(context.getId()))
                .orElseThrow(() -> new ResourceNotFoundException(ResponseErrorCodes.ITEM_NOT_FOUND));

        if (Boolean.TRUE.equals(address.getIsDefault())) {
            throw new DefaultAddressUpdateNotAllowedException(DEFAULT_ADDRESS_CAN_NOT_BE_CHANGE);
        }

        UpdateAddressRequestDTO.updateEntity(address, request);
        AddressEntity updated = addressRepository.save(address);
        return AddressResponseDTO.fromEntity(updated);
    }

    public List<AddressResponseDTO> getAllAddresses() {
        var context = UserContextHolder.getRequiredUser();

        return addressRepository.findByUserId(context.getId())
                .stream()
                .map(AddressResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
