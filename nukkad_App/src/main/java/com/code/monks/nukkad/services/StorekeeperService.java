package com.code.monks.nukkad.services;


import com.code.monks.nukkad.context.UserContextHolder;
import com.code.monks.nukkad.dto.request.CreateStorekeeperRequestDTO;
import com.code.monks.nukkad.dto.response.CreateStorekeeperResponseDTO;
import com.code.monks.nukkad.entities.StorekeeperEntity;
import com.code.monks.nukkad.exception.ResourceNotFoundException;
import com.code.monks.nukkad.repositories.StorekeeperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StorekeeperService {

    private final StorekeeperRepository storekeeperRepository;

    public CreateStorekeeperResponseDTO createStoreKeeper(CreateStorekeeperRequestDTO dto) {
        Long userId = UserContextHolder.getUser().getId();
        StorekeeperEntity storekeeper = CreateStorekeeperRequestDTO.toEntity(dto);
        storekeeper.setId(userId);
        StorekeeperEntity saved = storekeeperRepository.save(storekeeper);
        return CreateStorekeeperResponseDTO.fromEntity(saved);
    }

    public CreateStorekeeperResponseDTO updateStoreKeeper( CreateStorekeeperRequestDTO dto){
        Long userId = UserContextHolder.getUser().getId();
        StorekeeperEntity storekeeper = storekeeperRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("Storekeeper not found" + userId));

       storekeeper.setName(dto.getName());
       storekeeper.setStoreName(dto.getStoreName());
       storekeeper.setGstIn(dto.getGstIn());
       storekeeper.setAddressLine1(dto.getAddressLine1());
       storekeeper.setAddressLine2(dto.getAddressLine2());
       storekeeper.setContactNumber(dto.getContactNumber());
       storekeeper.setCity(dto.getCity());
       storekeeper.setState(dto.getState());
       storekeeper.setPincode(dto.getPincode());

       StorekeeperEntity updated = storekeeperRepository.save(storekeeper);
       return  CreateStorekeeperResponseDTO.fromEntity(updated);
    }

}
