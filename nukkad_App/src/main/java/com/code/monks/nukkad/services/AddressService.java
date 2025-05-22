package com.code.monks.nukkad.services;

import com.code.monks.nukkad.dto.request.CreateAddressRequestDTO;
import com.code.monks.nukkad.dto.request.UpdateAddressRequestDTO;
import com.code.monks.nukkad.dto.response.AddressResponseDTO;
import com.code.monks.nukkad.entities.AddressEntity;
import com.code.monks.nukkad.exception.ResourceNotFoundException;
import com.code.monks.nukkad.repositories.AddressRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public List<AddressResponseDTO>  getAllAddressByCustomer(int customerId){
        return addressRepository.findAllByCustomerId(customerId)
                        .stream().map(AddressResponseDTO::fromEntity).collect(Collectors.toList());
    }

    public AddressResponseDTO addAddress(int customerId, CreateAddressRequestDTO createAddressRequestDTO){
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setLabel(createAddressRequestDTO.getLabel());
        addressEntity.setAddressLine1(createAddressRequestDTO.getAddressLine1());
        addressEntity.setAddressLine2(createAddressRequestDTO.getAddressLine2());
        addressEntity.setLandmark(createAddressRequestDTO.getLandmark());
        addressEntity.setCity(createAddressRequestDTO.getCity());
        addressEntity.setState(createAddressRequestDTO.getState());
        addressEntity.setPincode(createAddressRequestDTO.getPincode());
        addressEntity.setId(customerId);
        addressEntity.setSelected(false);

        AddressEntity saved = addressRepository.save(addressEntity);
        return AddressResponseDTO.fromEntity(saved);
    }

    public AddressResponseDTO updateAddress( int customerId , int addressId , UpdateAddressRequestDTO updateAddressRequestDTO){

        AddressEntity entity = addressRepository.findByIdAndCustomerId(addressId, customerId)
                .orElseThrow(()-> new ResourceNotFoundException("Address not found"));


        entity.setLabel(updateAddressRequestDTO.getLabel());
        entity.setAddressLine1(updateAddressRequestDTO.getAddressLine1());
        entity.setAddressLine2(updateAddressRequestDTO.getAddressLine2());
        entity.setLandmark(updateAddressRequestDTO.getLandmark());
        entity.setCity(updateAddressRequestDTO.getCity());
        entity.setState(updateAddressRequestDTO.getState());
        entity.setPincode(updateAddressRequestDTO.getPincode());

        return AddressResponseDTO.fromEntity(addressRepository.save(entity));
    }




}
