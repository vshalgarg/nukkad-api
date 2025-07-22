package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.dto.request.BulkCreateCategoryRequestDTO;
import com.code.monks.nukkad.dto.request.UpdateCategoryRequestDTO;
import com.code.monks.nukkad.dto.response.BulkCreateCategoryResponseDTO;
import com.code.monks.nukkad.dto.response.CreateCategoryResponseDTO;
import com.code.monks.nukkad.dto.response.GetAllCategoryResponseDTO;
import com.code.monks.nukkad.dto.response.UpdateCategoryResponseDTO;
import com.code.monks.nukkad.services.CategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;


import java.util.List;

import static com.code.monks.nukkad.constants.UrlConstants.CATEGORY;
import static com.code.monks.nukkad.constants.UrlConstants.CATEGORY.GET_BY_ID;

@Slf4j
@RestController
@RequestMapping(CATEGORY.BASE)
@AllArgsConstructor
public class CategoryController {

	private final CategoryService categoryService;

	@PostMapping(CATEGORY.CREATE)
	public ResponseEntity<BulkCreateCategoryResponseDTO> createCategories(@RequestBody BulkCreateCategoryRequestDTO request) {
		log.info("[CREATE CATEGORY] Bulk create request received: {}", request);
		BulkCreateCategoryResponseDTO response = categoryService.createBulkCategories(request.getCategories());
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PutMapping(CATEGORY.UPDATE)
	public ResponseEntity<UpdateCategoryResponseDTO> updateCategory(
			@RequestParam Long id,
			@RequestBody UpdateCategoryRequestDTO request) {
		log.info("[UPDATE CATEGORY] Request to update category with id: {}", id);
		UpdateCategoryResponseDTO response = categoryService.updateCategory(id, request);
		return ResponseEntity.ok(response);
	}



	@GetMapping(CATEGORY.GET_ALL)
	public ResponseEntity<Page<GetAllCategoryResponseDTO>> getAllCategories(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "name") String sortBy,
			@RequestParam(defaultValue = "true") boolean isAsc
	) {
		log.info("[GET ALL CATEGORIES] Fetching categories with page={}, size={}, sortBy={}, isAsc={}", page, size, sortBy, isAsc);
		Page<GetAllCategoryResponseDTO> categories = categoryService.getAllCategories(page, size, sortBy, isAsc);
		log.info("[GET ALL CATEGORIES] Total categories fetched: {}", categories.getTotalElements());
		return ResponseEntity.ok(categories);
	}

	@GetMapping(GET_BY_ID)
	public ResponseEntity<CreateCategoryResponseDTO> getCategoryById(@PathVariable Long id) {
		log.info("[GET CATEGORY BY ID] Fetching category with id: {}", id);
		CreateCategoryResponseDTO response = categoryService.getById(id);
		return ResponseEntity.ok(response);
	}


}
