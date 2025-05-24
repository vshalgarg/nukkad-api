package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.dto.request.CreateShopkeeperRequestDTO;
import com.code.monks.nukkad.dto.response.CreateShopkeeperResponseDTO;
import com.code.monks.nukkad.services.ShopkeeperService;
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

	private  final ShopkeeperService shopkeeperService;

	@PostMapping(SHOPKEEPER.CREATE)
	public ResponseEntity<CreateShopkeeperResponseDTO> createShopkeeper(@Valid @RequestBody CreateShopkeeperRequestDTO dto) {
		log.info("Received request to createShopkeeper: {}", dto);
		CreateShopkeeperResponseDTO createdShopkeeper = shopkeeperService.createShopkeeper(dto);
		return new ResponseEntity<>(createdShopkeeper, HttpStatus.CREATED);
	}

	@PutMapping(SHOPKEEPER.UPDATE)
	public ResponseEntity<CreateShopkeeperResponseDTO> updateShopkeeper(@PathVariable("id") Long id,
																		@Valid @RequestBody CreateShopkeeperRequestDTO dto) {
		log.info("Received request to updateShopkeeper with id {}: {}", id, dto);
		CreateShopkeeperResponseDTO updatedShopkeeper = shopkeeperService.updateShopkeeper(id, dto);
		return ResponseEntity.ok(updatedShopkeeper);
	}

}
