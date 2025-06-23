package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.constants.UrlConstants;
import com.code.monks.nukkad.dto.request.CreateAddressRequestDTO;
import com.code.monks.nukkad.dto.request.UpdateAddressRequestDTO;
import com.code.monks.nukkad.dto.response.AddressResponseDTO;
import com.code.monks.nukkad.services.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(UrlConstants.ADDRESS.BASE)
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping(UrlConstants.ADDRESS.CREATE)
    public ResponseEntity<AddressResponseDTO> createAddress(@RequestBody CreateAddressRequestDTO request) {
        log.info("Received request to create new address.");
        AddressResponseDTO response = addressService.createAddress(request);
        log.info("Address created successfully. addressId={}", response.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(UrlConstants.ADDRESS.UPDATE)
    public ResponseEntity<AddressResponseDTO> updateAddress(@PathVariable Long id,
                                                            @RequestBody UpdateAddressRequestDTO request) {
        log.info("Received request to update address. addressId={}", id);
        AddressResponseDTO response = addressService.updateAddress(id, request);
        log.info("Address updated successfully. addressId={}", id);
        return ResponseEntity.ok(response);
    }

    @GetMapping(UrlConstants.ADDRESS.GET)
    public ResponseEntity<List<AddressResponseDTO>> getAllAddresses() {
        log.info("Received request to fetch all addresses for current user.");
        List<AddressResponseDTO> responses = addressService.getAllAddresses();
        log.info("Fetched {} address(es).", responses.size());
        return ResponseEntity.ok(responses);
    }

    @PutMapping(UrlConstants.ADDRESS.MARK_AS_DEFAULT)
    public ResponseEntity<String> markAsDefault(@PathVariable Long id) {
        log.info("Received request to mark address as default. addressId={}", id);
        addressService.markAsDefault(id);
        log.info("Address marked as default successfully. addressId={}", id);
        return ResponseEntity.ok("Address marked as default");
    }

    @DeleteMapping(UrlConstants.ADDRESS.DELETE)
    public ResponseEntity<String> deleteAddress(@PathVariable Long id) {
        log.info("Received request to delete address. addressId={}", id);
        addressService.deleteAddress(id);
        log.info("Address deleted successfully. addressId={}", id);
        return ResponseEntity.ok("Address deleted successfully");
    }
}
