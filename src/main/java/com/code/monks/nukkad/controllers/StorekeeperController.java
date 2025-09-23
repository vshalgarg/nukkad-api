package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.dto.request.StorekeeperRequestDTO;
import com.code.monks.nukkad.dto.response.StorekeeperResponseDTO;
import com.code.monks.nukkad.dto.response.GetStorekeeperProfileResponseDTO;
import com.code.monks.nukkad.services.StorekeeperService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.code.monks.nukkad.constants.UrlConstants.STOREKEEPER;

@Slf4j
@RestController
@RequestMapping(STOREKEEPER.BASE)
@AllArgsConstructor
public class StorekeeperController {

    private final StorekeeperService storekeeperService;


    @PostMapping(value = STOREKEEPER.CREATE)
    public ResponseEntity<StorekeeperResponseDTO> createStorekeeper(
            @RequestBody @Valid StorekeeperRequestDTO dto) {

        log.info("Creating storekeeper with images...");

        StorekeeperResponseDTO response = storekeeperService.createStoreKeeper(dto);

        log.info("Storekeeper created successfully.");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = STOREKEEPER.UPDATE)
    public ResponseEntity<StorekeeperResponseDTO> updateStorekeeper(
            @RequestBody @Valid StorekeeperRequestDTO dto){

        log.info("Updating storekeeper profile...");

        StorekeeperResponseDTO response = storekeeperService.updateStoreKeeper(dto);

        log.info("Storekeeper updated successfully.");

        return ResponseEntity.ok(response);
    }

    @GetMapping(STOREKEEPER.GET_PROFILE)
    public ResponseEntity<GetStorekeeperProfileResponseDTO> getProfile() {
        log.info("[GET PROFILE] Request to fetch storekeeper profile");

        GetStorekeeperProfileResponseDTO response = storekeeperService.getStorekeeperProfile();

        log.info("[GET PROFILE] Profile fetched for storekeeperId={}", response.getId());
        return ResponseEntity.ok(response);
    }

}
