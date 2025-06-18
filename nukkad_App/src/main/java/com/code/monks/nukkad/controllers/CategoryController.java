package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.dto.request.CreateCategoryRequestDTO;
import com.code.monks.nukkad.dto.response.CreateCategoryResponseDTO;
import com.code.monks.nukkad.dto.response.GetAllCategoryResponseDTO;
import com.code.monks.nukkad.services.CategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.code.monks.nukkad.constants.UrlConstants.CATEGORY;

@Slf4j
@RestController
@RequestMapping(CATEGORY.BASE)
@AllArgsConstructor
public class CategoryController {

	private final CategoryService categoryService;

	@PostMapping(CATEGORY.CREATE)
	public ResponseEntity<CreateCategoryResponseDTO> createCategory(@RequestBody CreateCategoryRequestDTO request) {
		log.info("[CREATE CATEGORY] Request received: {}", request);
		CreateCategoryResponseDTO response = categoryService.createCategory(request);
		log.info("[CREATE CATEGORY] Successfully created category with ID: {}", response.getId());
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping(CATEGORY.GET_ALL)
	public ResponseEntity<List<GetAllCategoryResponseDTO>> getAllCategories() {
		log.info("[GET ALL CATEGORIES] Fetching all categories.");
		List<GetAllCategoryResponseDTO> categories = categoryService.getAllCategories();
		log.info("[GET ALL CATEGORIES] Total categories found: {}", categories.size());
		return ResponseEntity.ok(categories);
	}
}
