package com.code.monks.nukkad.services;

import com.code.monks.nukkad.dto.request.CreateCategoryRequestDTO;
import com.code.monks.nukkad.dto.response.CreateCategoryResponseDTO;
import com.code.monks.nukkad.dto.response.GetAllCategoryResponseDTO;
import com.code.monks.nukkad.entities.CategoryEntity;
import com.code.monks.nukkad.exception.DuplicateResourceException;
import com.code.monks.nukkad.exception.ResourceNotFoundException;
import com.code.monks.nukkad.exception.UnhandledException;
import com.code.monks.nukkad.mapper.CategoryMapper;
import com.code.monks.nukkad.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.code.monks.nukkad.enums.ResponseErrorCodes.DUPLICATE_CATEGORY_EXCEPTION;
import static com.code.monks.nukkad.enums.ResponseErrorCodes.UNHANDLED_EXCEPTION;

@Service
@Slf4j
@AllArgsConstructor
public class CategoryService {

	private CategoryRepository categoryRepository;

	public CreateCategoryResponseDTO createCategory(CreateCategoryRequestDTO categoryDto) {
		log.info("Attempting to save new category: {}", categoryDto);
		try {
			CategoryEntity category = new CategoryEntity();
			category.setName(categoryDto.getName());
			CategoryEntity savedEntity = categoryRepository.save(category);
			log.info("category successfully saved to db: {}", savedEntity);
			return CreateCategoryResponseDTO.fromDbDto(savedEntity);
		}
		catch (DataIntegrityViolationException e) {
			log.error("Duplicated category name was found: {}", categoryDto, e);
			throw new DuplicateResourceException(DUPLICATE_CATEGORY_EXCEPTION);
		}
		catch (Exception e) {
			log.error("Unhandled exception: {}", categoryDto, e);
			throw new UnhandledException(UNHANDLED_EXCEPTION, e);
		}
	}

	public List<GetAllCategoryResponseDTO> getAllCategories() {
		try {
			log.info("Fetching all categories from the database.");
			List<CategoryEntity> categories = categoryRepository.findAll();
			if (CollectionUtils.isEmpty(categories)) {
				log.warn("No category found in DB");
				return Collections.emptyList();
			}
			log.info("Total categories found: {}", categories.size());
			return categories.stream().map(GetAllCategoryResponseDTO::fromDbDto).toList();
		}
		catch (Exception e) {
			throw new UnhandledException(UNHANDLED_EXCEPTION, e);
		}
	}

	/*
	 *
	 * update Category
	 *
	 */
	public GetCategoryRequestDTO updateCategory(Long id, CreateCategoryRequestDTO dto) {
		log.info("Updating category with ID: {}", id);

		CategoryEntity existingCategory = categoryRepository.findById(id).orElseThrow(() -> {
			log.error("Category not found with ID: {}", id);
			return new ResourceNotFoundException("Category not found with id: " + id);
		});

		log.debug("Existing category before update: {}", existingCategory);

		existingCategory.setCategoryName(dto.getCategoryName());
		CategoryEntity updatedCategory = categoryRepository.save(existingCategory);

		log.info("Category successfully updated: {}", updatedCategory);

		GetCategoryRequestDTO updatedCategoryDTO = new GetCategoryRequestDTO();
		updatedCategoryDTO.setCategoryId(updatedCategory.getCategoryId());
		updatedCategoryDTO.setCategoryName(updatedCategory.getCategoryName());

		return updatedCategoryDTO;
	}

	/*
	 *
	 * delete category
	 *
	 */
	// public String deleteCategory(Long id) {
	// Category category = categoryRepository.findById(id)
	// .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
	//
	// // Delete the category
	// categoryRepository.delete(category);
	//
	// return "Category with ID " + id + " has been successfully deleted.";
	// }

}
