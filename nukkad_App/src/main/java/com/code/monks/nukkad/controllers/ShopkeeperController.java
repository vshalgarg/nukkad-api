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

import static com.code.monks.nukkad.constants.UrlConstants.*;

@RestController
@RequestMapping(SHOPKEEPER.BASE)
@Slf4j
@AllArgsConstructor
public class ShopkeeperController {

	private  final StorekeeperService shopkeeperService;

	@PostMapping(SHOPKEEPER.CREATE)
	public ResponseEntity<CreateStorekeeperResponseDTO> createShopkeeper(@Valid @RequestBody CreateStorekeeperRequestDTO dto) {
		log.info("Received request to createShopkeeper: {}", dto);
		CreateStorekeeperResponseDTO createdShopkeeper = shopkeeperService.createShopkeeper(dto);
		return new ResponseEntity<>(createdShopkeeper, HttpStatus.CREATED);
	}

	@PutMapping(SHOPKEEPER.UPDATE)
	public ResponseEntity<CreateStorekeeperResponseDTO> updateShopkeeper(@PathVariable("id") Long id,
																		 @Valid @RequestBody CreateStorekeeperRequestDTO dto) {
		log.info("Received request to updateShopkeeper with id {}: {}", id, dto);
		CreateStorekeeperResponseDTO updatedShopkeeper = shopkeeperService.updateShopkeeper(id, dto);
		return ResponseEntity.ok(updatedShopkeeper);
	}

}
