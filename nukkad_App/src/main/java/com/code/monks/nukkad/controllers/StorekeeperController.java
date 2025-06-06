package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.dto.request.CreateStorekeeperRequestDTO;
import com.code.monks.nukkad.dto.response.CreateStorekeeperResponseDTO;
import com.code.monks.nukkad.services.StorekeeperService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.code.monks.nukkad.constants.UrlConstants.STOREKEEPER;

@RestController
@RequestMapping(STOREKEEPER.BASE)
@Slf4j
@AllArgsConstructor
public class StorekeeperController {

	private final StorekeeperService storekeeperService;

    @PostMapping(STOREKEEPER.CREATE)
    public ResponseEntity<CreateStorekeeperResponseDTO> createStorekeeper(
            @Valid @RequestBody CreateStorekeeperRequestDTO dto
            ) {
        CreateStorekeeperResponseDTO saveStorekeeperInDb = storekeeperService.createStoreKeeper(dto);
        return new ResponseEntity<>(saveStorekeeperInDb, HttpStatus.CREATED);
    }

    @PutMapping(STOREKEEPER.UPDATE)
    public ResponseEntity<CreateStorekeeperResponseDTO> updateCustomer(
            @Valid @RequestBody CreateStorekeeperRequestDTO dto) {
        CreateStorekeeperResponseDTO updateStorekeeper = storekeeperService.updateStoreKeeper(dto);
        return new ResponseEntity<>(updateStorekeeper,HttpStatus.CREATED);
    }
}
