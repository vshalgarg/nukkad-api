package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.dto.request.CreateItemRequestDTO;
import com.code.monks.nukkad.dto.response.CreateItemResponseDTO;
import com.code.monks.nukkad.dto.response.GetAllItemResponseDTO;
import com.code.monks.nukkad.services.ItemService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.code.monks.nukkad.constants.UrlConstants.*;

@Slf4j
@RestController
@RequestMapping(ITEM.BASE)
@AllArgsConstructor
public class ItemController {

	private final ItemService itemService;

	@PostMapping(ITEM.CREATE)
	public ResponseEntity<CreateItemResponseDTO> createItem(@Valid @RequestBody CreateItemRequestDTO dto) {
		log.info("[CREATE ITEM] Request received: {}", dto);
		CreateItemResponseDTO response = itemService.createItem(dto);
		log.info("[CREATE ITEM] Item created with ID: {}", response.getId());
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping(ITEM.GET_ALL)
	public ResponseEntity<List<GetAllItemResponseDTO>> getAllItems() {
		log.info("[GET ALL ITEMS] Fetching all items.");
		List<GetAllItemResponseDTO> items = itemService.getAllItems();
		log.info("[GET ALL ITEMS] Total items found: {}", items.size());
		return ResponseEntity.ok(items);
	}

	@GetMapping(ITEM.GET_BY_ID)
	public ResponseEntity<CreateItemResponseDTO> getItemById(@PathVariable Long id) {
		log.info("[GET ITEM BY ID] Fetching item with ID: {}", id);
		CreateItemResponseDTO item = itemService.getItemById(id);
		log.info("[GET ITEM BY ID] Found item: {}", item.getName());
		return ResponseEntity.ok(item);
	}

	@PutMapping(ITEM.UPDATE)
	public ResponseEntity<CreateItemResponseDTO> updateItem(@PathVariable Long id, @RequestBody CreateItemRequestDTO dto) {
		log.info("[UPDATE ITEM] Updating item with ID: {} with data: {}", id, dto);
		CreateItemResponseDTO updatedItem = itemService.updateItem(id, dto);
		log.info("[UPDATE ITEM] Updated item with ID: {}", updatedItem.getId());
		return ResponseEntity.ok(updatedItem);
	}
}
