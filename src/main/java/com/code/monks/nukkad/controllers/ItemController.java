package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.dto.request.BulkCreateItemRequestDTO;
import com.code.monks.nukkad.dto.request.UpdateItemRequestDTO;
import com.code.monks.nukkad.dto.response.*;
import com.code.monks.nukkad.services.ItemService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
	public ResponseEntity<Page<GetAllItemResponseDTO>> getAllItems(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "id") String sortBy
	) {
		log.info("[GET ALL ITEMS] Fetching items with pagination and sorting.");
		Page<GetAllItemResponseDTO> items = itemService.getAllItems(page, size, sortBy);
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
	public ResponseEntity<GetItemsByCategoryResponseDTO> getItemsByCategory(
			@PathVariable Long categoryId,
			@PageableDefault(page = 0, size = 10)
			@SortDefault.SortDefaults({
					@SortDefault(sort = "name", direction = Sort.Direction.ASC)
			}) Pageable pageable
	) {
		log.info("Getting items for categoryId={} with pageable={}", categoryId, pageable);

		GetItemsByCategoryResponseDTO response = itemService.getItemsByCategory(categoryId, pageable);
		return ResponseEntity.ok(response);
	}

}
