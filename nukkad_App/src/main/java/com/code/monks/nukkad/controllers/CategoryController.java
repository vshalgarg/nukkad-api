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

import static com.code.monks.nukkad.constants.UrlConstants.*;
import static com.code.monks.nukkad.constants.UrlConstants.CATEGORY.GET_ALL;

@RestController
@RequestMapping(CATEGORY.BASE)
@Slf4j
@AllArgsConstructor
public class CategoryController {

	private CategoryService categoryService;

	@PostMapping(CATEGORY.CREATE)
	public ResponseEntity<CreateCategoryResponseDTO> createCategory(@RequestBody CreateCategoryRequestDTO category) {
		log.info("Received request for createCategory: {}", category);
		CreateCategoryResponseDTO categoryInDB = categoryService.createCategory(category);
		return new ResponseEntity<>(categoryInDB, HttpStatus.CREATED);
	}

	@GetMapping(GET_ALL)
	public ResponseEntity<List<GetAllCategoryResponseDTO>> getAllCategories() {
		List<GetAllCategoryResponseDTO> categories = categoryService.getAllCategories();
		return ResponseEntity.ok(categories);
	}

}

