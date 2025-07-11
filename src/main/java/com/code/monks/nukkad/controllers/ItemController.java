package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.dto.request.BulkCreateItemRequestDTO;
import com.code.monks.nukkad.dto.request.UpdateItemRequestDTO;
import com.code.monks.nukkad.dto.response.*;
import com.code.monks.nukkad.services.ItemService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.code.monks.nukkad.constants.UrlConstants.ITEM;
import static com.code.monks.nukkad.constants.UrlConstants.ITEM.GET_BY_CATEGORY;

@Slf4j
@RestController
@RequestMapping(ITEM.BASE)
@AllArgsConstructor
public class ItemController {

	private final ItemService itemService;

	@PostMapping(ITEM.CREATE)
	public ResponseEntity<BulkCreateItemResponseDTO> createItem(@Valid @RequestBody BulkCreateItemRequestDTO request) {
		log.info("[CREATE ITEMS] Bulk create request received: {}", request);
		BulkCreateItemResponseDTO response = itemService.createBulkItems(request.getItems());
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
	public ResponseEntity<UpdateItemResponseDTO> updateItem(
			@PathVariable Long id,
			@Valid @RequestBody UpdateItemRequestDTO dto) {
		log.info("[UPDATE ITEM] Updating item with ID: {} with data: {}", id, dto);
		UpdateItemResponseDTO updatedItem = itemService.updateItem(id, dto);
		log.info("[UPDATE ITEM] Updated item with ID: {}", updatedItem.getId());
		return ResponseEntity.ok(updatedItem);
	}

	@GetMapping(GET_BY_CATEGORY)
	public ResponseEntity<GetItemsByCategoryResponseDTO> getItemsByCategory(@PathVariable Long categoryId) {
		log.info("Getting items for categoryId={}", categoryId);

		GetItemsByCategoryResponseDTO response = itemService.getItemsByCategory(categoryId);

		return ResponseEntity.ok(response);
	}

}
