package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.dto.request.CreateAddressRequestDTO;
import com.code.monks.nukkad.dto.request.UpdateAddressRequestDTO;
import com.code.monks.nukkad.dto.response.AddressResponseDTO;
import com.code.monks.nukkad.services.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.code.monks.nukkad.constants.UrlConstants.ADDRESS;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
@Slf4j
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<List<AddressResponseDTO>> getAddressesByCustomerId(){
        List<AddressResponseDTO> addresses = addressService.getAddresses();
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AddressResponseDTO> addAddress( @Valid
            @RequestBody CreateAddressRequestDTO dto) {

        AddressResponseDTO savedAddress = addressService.addAddress(dto);

        return new ResponseEntity<>(savedAddress , HttpStatus.CREATED);
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressResponseDTO> updateAddress(
            @PathVariable Long addressId,
            @Valid @RequestBody UpdateAddressRequestDTO dto) {

        log.info("Updating address with ID {}", addressId);
        AddressResponseDTO updatedAddress = addressService.updateAddress(addressId, dto);
        return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }
}
