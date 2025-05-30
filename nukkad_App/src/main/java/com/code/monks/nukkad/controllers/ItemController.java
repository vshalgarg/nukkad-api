package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.dto.request.CreateItemRequestDTO;
import com.code.monks.nukkad.dto.response.CreateItemResponseDTO;
import com.code.monks.nukkad.dto.response.GetAllItemResponseDTO;
import com.code.monks.nukkad.services.ItemService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.code.monks.nukkad.constants.UrlConstants.*;

@RestController
@RequestMapping(ITEM.BASE)
@AllArgsConstructor
public class ItemController {

	private final ItemService itemService;

	@PostMapping(ITEM.CREATE)
	public ResponseEntity<CreateItemResponseDTO> createItem(@RequestBody CreateItemRequestDTO dto) {
 		return ResponseEntity.ok(itemService.createItem(dto));
	}

	@GetMapping(ITEM.GET_ALL)
	public ResponseEntity<List<GetAllItemResponseDTO>> getAllItems() {
		return ResponseEntity.ok(itemService.getAllItems());
	}

	@GetMapping(ITEM.GET_BY_ID)
	public ResponseEntity<CreateItemResponseDTO> getItemById(@PathVariable int id) {
		return ResponseEntity.ok(itemService.getItemById(id));
	}

	@PutMapping(ITEM.UPDATE)
	public ResponseEntity<CreateItemResponseDTO> updateItem(@PathVariable int id, @RequestBody CreateItemRequestDTO dto) {
		return ResponseEntity.ok(itemService.updateItem(id, dto));
	}
}
