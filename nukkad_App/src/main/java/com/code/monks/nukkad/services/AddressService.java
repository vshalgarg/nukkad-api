<<<<<<< Updated upstream
package com.code.monks.nukkad.services;

import com.code.monks.nukkad.context.RequestContextHolder;
import com.code.monks.nukkad.dto.request.CreateAddressRequestDTO;
import com.code.monks.nukkad.dto.request.UpdateAddressRequestDTO;
import com.code.monks.nukkad.dto.response.AddressResponseDTO;
import com.code.monks.nukkad.entities.AddressEntity;
import com.code.monks.nukkad.exception.ResourceNotFoundException;
import com.code.monks.nukkad.exception.UnhandledException;
import com.code.monks.nukkad.repositories.AddressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.stream.Collectors;

import static com.code.monks.nukkad.enums.ResponseErrorCodes.UNHANDLED_EXCEPTION;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressService {

    private final AddressRepository addressRepository;

    public List<AddressResponseDTO> getAddresses() {
        long customerId= RequestContextHolder.getCustomer().getCustomerId();
        log.info("Fetching all addresses for customerId: {}", customerId);

        List<AddressResponseDTO> addressList = addressRepository.findAllByCustomerId(customerId)
                .stream()
                .map(AddressResponseDTO::fromEntity)
                .collect(Collectors.toList());

        log.debug("Found {} addresses for customerId: {}", addressList.size(), customerId);
        return addressList;
    }

    public AddressResponseDTO addAddress(CreateAddressRequestDTO dto) {
        try {
            long customerId = RequestContextHolder.getCustomer().getCustomerId();
            log.info("Adding new address for customerId: {}", customerId);

            AddressEntity entity = CreateAddressRequestDTO.toEntity(dto);
            entity.setCustomerId(customerId);

            AddressEntity saved = addressRepository.save(entity);
            log.debug("Saved new address with id: {}", saved.getId());

            return AddressResponseDTO.fromEntity(saved);
        }

        catch (UnhandledException e){

            throw new UnhandledException(UNHANDLED_EXCEPTION, e);
        }
    }

    public AddressResponseDTO updateAddress(Long addressId, UpdateAddressRequestDTO dto) {
        long customerId= RequestContextHolder.getCustomer().getCustomerId();
        log.info("Updating address with id: {} for customerId: {}", addressId, customerId);

        AddressEntity address = addressRepository.findByIdAndCustomerId(addressId, customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found or unauthorized"));

        dto.updateEntity(address);
        AddressEntity updated = addressRepository.save(address);

        log.debug("Updated address with id: {}", updated.getId());
        return AddressResponseDTO.fromEntity(updated);
    }
}
=======
//package com.code.monks.nukkad.services;
//
//import com.code.monks.nukkad.dto.request.CreateAddressRequestDTO;
//import com.code.monks.nukkad.dto.request.UpdateAddressRequestDTO;
//import com.code.monks.nukkad.dto.response.AddressResponseDTO;
//import com.code.monks.nukkad.entities.AddressEntity;
//import com.code.monks.nukkad.exception.ResourceNotFoundException;
//import com.code.monks.nukkad.repositories.AddressRepository;
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@AllArgsConstructor
//public class AddressService {
//
//    private final AddressRepository addressRepository;
//
//    public List<AddressResponseDTO>  getAllAddressByCustomer(int customerId){
//        return addressRepository.findAllByCustomerId(customerId)
//                        .stream().map(AddressResponseDTO::fromEntity).collect(Collectors.toList());
//    }
//
//    public AddressResponseDTO addAddress(int customerId, CreateAddressRequestDTO createAddressRequestDTO){
//        AddressEntity addressEntity = new AddressEntity();
//        addressEntity.setLabel(createAddressRequestDTO.getLabel());
//        addressEntity.setAddressLine1(createAddressRequestDTO.getAddressLine1());
//        addressEntity.setAddressLine2(createAddressRequestDTO.getAddressLine2());
//        addressEntity.setLandmark(createAddressRequestDTO.getLandmark());
//        addressEntity.setCity(createAddressRequestDTO.getCity());
//        addressEntity.setState(createAddressRequestDTO.getState());
//        addressEntity.setPincode(createAddressRequestDTO.getPincode());
//        addressEntity.setId(customerId);
//        addressEntity.setSelected(false);
//
//        AddressEntity saved = addressRepository.save(addressEntity);
//        return AddressResponseDTO.fromEntity(saved);
//    }
//
//    public AddressResponseDTO updateAddress( int customerId , int addressId , UpdateAddressRequestDTO updateAddressRequestDTO){
//
//        AddressEntity entity = addressRepository.findByIdAndCustomerId(addressId, customerId)
//                .orElseThrow(()-> new ResourceNotFoundException("Address not found"));
//
//
//        entity.setLabel(updateAddressRequestDTO.getLabel());
//        entity.setAddressLine1(updateAddressRequestDTO.getAddressLine1());
//        entity.setAddressLine2(updateAddressRequestDTO.getAddressLine2());
//        entity.setLandmark(updateAddressRequestDTO.getLandmark());
//        entity.setCity(updateAddressRequestDTO.getCity());
//        entity.setState(updateAddressRequestDTO.getState());
//        entity.setPincode(updateAddressRequestDTO.getPincode());
//
//        return AddressResponseDTO.fromEntity(addressRepository.save(entity));
//    }
//
//
//
//
//}
>>>>>>> Stashed changes

public void main() {
}
