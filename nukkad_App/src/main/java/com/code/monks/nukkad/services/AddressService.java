
package com.code.monks.nukkad.services;

import com.code.monks.nukkad.context.UserContextHolder;
import com.code.monks.nukkad.dto.User;
import com.code.monks.nukkad.dto.request.CreateAddressRequestDTO;
import com.code.monks.nukkad.dto.request.UpdateAddressRequestDTO;
import com.code.monks.nukkad.dto.response.AddressResponseDTO;
import com.code.monks.nukkad.entities.AddressEntity;
import com.code.monks.nukkad.entities.CustomerEntity;
import com.code.monks.nukkad.entities.StorekeeperEntity;
import com.code.monks.nukkad.enums.RoleEnum;
import com.code.monks.nukkad.exception.ResourceNotFoundException;
import com.code.monks.nukkad.exception.UnhandledException;
import com.code.monks.nukkad.repositories.AddressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.code.monks.nukkad.enums.ResponseErrorCodes.UNHANDLED_EXCEPTION;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressService {

    private final AddressRepository addressRepository;

    public List<AddressResponseDTO> getAddresses() {
        User user = UserContextHolder.getUser();  // get user from context
        Long userId = user.getId();

        log.info("Fetching all addresses for userId: {}", userId);

        List<AddressEntity> addresses;

        if (user.getRole() == RoleEnum.CUSTOMER) {
            addresses = addressRepository.findAllByCustomer_Id(userId);
        } else if (user.getRole() == RoleEnum.STOREKEEPER) {
            addresses = addressRepository.findAllByStorekeeper_Id(userId);
        } else {
            throw new IllegalStateException("Unsupported role: " + user.getRole());
        }

        List<AddressResponseDTO> addressList = addresses.stream()
                .map(AddressResponseDTO::fromEntity)
                .collect(Collectors.toList());

        log.debug("Found {} addresses for userId: {}", addressList.size(), userId);
        return addressList;
    }


    public AddressResponseDTO addAddress(CreateAddressRequestDTO dto) {
        try {
            User user = UserContextHolder.getUser();
            log.info("Adding new address for userId: {}", user.getId());

            AddressEntity entity = CreateAddressRequestDTO.toEntity(dto);

            if (user.getRole() == RoleEnum.CUSTOMER) {
                CustomerEntity customer = new CustomerEntity();
                customer.setId(user.getId());
                entity.setCustomer(customer);
            } else if (user.getRole() == RoleEnum.STOREKEEPER) {
                StorekeeperEntity storekeeper = new StorekeeperEntity();
                storekeeper.setId(user.getId());
                entity.setStorekeeper(storekeeper);
            }

            AddressEntity saved = addressRepository.save(entity);
            log.debug("Saved new address with id: {}", saved.getId());

            return AddressResponseDTO.fromEntity(saved);
        } catch (UnhandledException e) {
            throw new UnhandledException(UNHANDLED_EXCEPTION, e);
        }
    }


    public AddressResponseDTO updateAddress(Long addressId, UpdateAddressRequestDTO dto) {
        User user = UserContextHolder.getUser();
        log.info("Updating address with id: {} for userId: {}", addressId, user.getId());

        AddressEntity address;

        if (user.getRole() == RoleEnum.CUSTOMER) {
            address = addressRepository.findByIdAndCustomer_Id(addressId, user.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Address not found or unauthorized for customer"));
        } else if (user.getRole() == RoleEnum.STOREKEEPER) {
            address = addressRepository.findByIdAndStorekeeper_Id(addressId, user.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Address not found or unauthorized for storekeeper"));
        } else {
            throw new IllegalStateException("Unsupported role: " + user.getRole());
        }

        dto.updateEntity(address); //
        AddressEntity updated = addressRepository.save(address);

        log.debug("Updated address with id: {}", updated.getId());
        return AddressResponseDTO.fromEntity(updated);
    }

}


