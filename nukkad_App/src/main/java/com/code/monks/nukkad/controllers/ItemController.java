package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.dto.ItemRequestDTO;
import com.code.monks.nukkad.dto.ItemResponseDTO;
import com.code.monks.nukkad.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.code.monks.nukkad.constants.UrlConstants.*;

@RestController
@RequestMapping(ITEM)
public class ItemController {

	@Autowired
	private ItemService itemService;

	/*
	 *
	 * Create new item
	 *
	 */
	@PostMapping(SAVE_ITEM)
	public ResponseEntity<ItemResponseDTO> createItem(@RequestBody ItemRequestDTO dto) {
		ItemResponseDTO savedItem = itemService.createItem(dto);
		return ResponseEntity.ok(savedItem);
	}

	/*
	 *
	 * Get all items
	 *
	 */
	@GetMapping(GET_ALL_ITEMS)
	public ResponseEntity<List<ItemResponseDTO>> getAllItems() {
		List<ItemResponseDTO> items = itemService.getAllItems();
		return ResponseEntity.ok(items);
	}

	/*
	 *
	 * Get item by ID
	 *
	 */
	@GetMapping(GET_ITEM_BY_ID)
	public ResponseEntity<ItemResponseDTO> getItemById(@PathVariable Long id) {
		ItemResponseDTO item = itemService.getItemById(id);
		return ResponseEntity.ok(item);
	}

	/*
	 *
	 * Update item
	 *
	 */
	@PutMapping(UPDATE_ITEM_BY_ID)
	public ResponseEntity<ItemResponseDTO> updateItem(@PathVariable Long id, @RequestBody ItemRequestDTO dto) {
		ItemResponseDTO updatedItem = itemService.updateItem(id, dto);
		return ResponseEntity.ok(updatedItem);
	}

}
