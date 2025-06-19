package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.constants.UrlConstants;
import com.code.monks.nukkad.dto.request.CreateAddressRequestDTO;
import com.code.monks.nukkad.dto.request.UpdateAddressRequestDTO;
import com.code.monks.nukkad.dto.response.AddressResponseDTO;
import com.code.monks.nukkad.services.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlConstants.ADDRESS.BASE)
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping(UrlConstants.ADDRESS.CREATE)
    public ResponseEntity<AddressResponseDTO> createAddress(@RequestBody CreateAddressRequestDTO request) {
        AddressResponseDTO response = addressService.createAddress(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(UrlConstants.ADDRESS.UPDATE)
    public ResponseEntity<AddressResponseDTO> updateAddress(@PathVariable Long id,
                                                            @RequestBody UpdateAddressRequestDTO request) {
        AddressResponseDTO response = addressService.updateAddress(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping(UrlConstants.ADDRESS.GET)
    public ResponseEntity<List<AddressResponseDTO>> getAllAddresses() {
        List<AddressResponseDTO> responses = addressService.getAllAddresses();
        return ResponseEntity.ok(responses);
    }

    @PutMapping(UrlConstants.ADDRESS.MARK_AS_DEFAULT)
    public ResponseEntity<String> markAsDefault(@PathVariable Long id) {
        addressService.markAsDefault(id);
        return ResponseEntity.ok("Address marked as default");
    }

    @DeleteMapping(UrlConstants.ADDRESS.DELETE)
    public ResponseEntity<String> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.ok("Address deleted successfully");
    }


}
