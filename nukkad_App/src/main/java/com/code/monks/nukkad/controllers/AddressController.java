package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.dto.response.AddressResponseDTO;
import com.code.monks.nukkad.services.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/addresses")
@AllArgsConstructor
public class AddressController {

    private final AddressService addressService;

    public ResponseEntity<List<AddressResponseDTO>> getAllAddressByCustome(){

    }
}
