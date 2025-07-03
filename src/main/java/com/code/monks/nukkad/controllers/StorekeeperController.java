package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.dto.request.CreateStorekeeperRequestDTO;
import com.code.monks.nukkad.dto.response.CreateStorekeeperResponseDTO;
import com.code.monks.nukkad.services.StorekeeperService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.code.monks.nukkad.constants.UrlConstants.STOREKEEPER;

@Slf4j
@RestController
@RequestMapping(STOREKEEPER.BASE)
@AllArgsConstructor
public class StorekeeperController {

    private final StorekeeperService storekeeperService;


    @PostMapping(value = STOREKEEPER.CREATE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CreateStorekeeperResponseDTO> createStorekeeper(
            @RequestPart("data") @Valid CreateStorekeeperRequestDTO dto,
            @RequestPart("images") MultipartFile[] images) {

        log.info("Creating storekeeper with images...");

        CreateStorekeeperResponseDTO response = storekeeperService.createStoreKeeper(dto, images);

        log.info("Storekeeper created successfully.");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PutMapping(value = STOREKEEPER.UPDATE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CreateStorekeeperResponseDTO> updateStorekeeper(
            @RequestPart("data") @Valid CreateStorekeeperRequestDTO dto,
            @RequestPart(value = "images", required = false) MultipartFile[] images) {

        log.info("Updating storekeeper profile...");

        CreateStorekeeperResponseDTO response = storekeeperService.updateStoreKeeper(dto, images);

        log.info("Storekeeper updated successfully.");

        return ResponseEntity.ok(response);
    }
}
