package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.dto.ShopkeeperRequestDTO;
import com.code.monks.nukkad.dto.ShopkeeperResponseDTO;
import com.code.monks.nukkad.services.ShopkeeperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.code.monks.nukkad.constants.UrlConstants.*;

@RestController
@RequestMapping(SHOPKEEPER)
public class ShopkeeperController {

	@Autowired
	private ShopkeeperService shopkeeperService;

	@PostMapping(SAVE_SHOPKEEPER_DETAILS)
	public ResponseEntity<ShopkeeperResponseDTO> createShopkeeper(@RequestBody ShopkeeperRequestDTO dto) {
		ShopkeeperResponseDTO createdShopkeeper = shopkeeperService.createShopkeeper(dto);
		return new ResponseEntity<>(createdShopkeeper, HttpStatus.CREATED);
	}

	@GetMapping(GET_SHOPKEEPER_DETAILS_BY_ID)
	public ResponseEntity<ShopkeeperResponseDTO> getShopkeeperById(@PathVariable Long id) {
		ShopkeeperResponseDTO shopkeeper = shopkeeperService.getById(id);
		return new ResponseEntity<>(shopkeeper, HttpStatus.OK);
	}

	@PutMapping(UPDATE_SHOPKEEPER_DETAILS)
	public ResponseEntity<ShopkeeperResponseDTO> updateShopkeeper(@PathVariable Long id,
			@RequestBody ShopkeeperRequestDTO dto) {
		ShopkeeperResponseDTO updatedShopkeeper = shopkeeperService.updateShopkeeper(id, dto);
		return new ResponseEntity<>(updatedShopkeeper, HttpStatus.OK);
	}

}
