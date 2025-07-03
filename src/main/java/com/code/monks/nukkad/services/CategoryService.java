package com.code.monks.nukkad.services;

import com.code.monks.nukkad.context.UserContextHolder;
import com.code.monks.nukkad.dto.User;
import com.code.monks.nukkad.dto.request.CreateCategoryRequestDTO;
import com.code.monks.nukkad.dto.request.UpdateCategoryRequestDTO;
import com.code.monks.nukkad.dto.response.BulkCreateCategoryResponseDTO;
import com.code.monks.nukkad.dto.response.CreateCategoryResponseDTO;
import com.code.monks.nukkad.dto.response.GetAllCategoryResponseDTO;
import com.code.monks.nukkad.dto.response.UpdateCategoryResponseDTO;
import com.code.monks.nukkad.entities.CategoryEntity;
import com.code.monks.nukkad.entities.CategoryItemImageEntity;
import com.code.monks.nukkad.enums.RoleEnum;
import com.code.monks.nukkad.exception.AccessDeniedException;
import com.code.monks.nukkad.exception.DuplicateResourceException;
import com.code.monks.nukkad.exception.ResourceNotFoundException;
import com.code.monks.nukkad.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.code.monks.nukkad.enums.ResponseErrorCodes.*;

@Service
@Slf4j
@AllArgsConstructor
public class   CategoryService {

	private final CategoryRepository categoryRepository;

	public BulkCreateCategoryResponseDTO createBulkCategories(List<CreateCategoryRequestDTO> requestList) {
		User admin = UserContextHolder.getRequiredUser();
		if (!admin.getRoles().contains(RoleEnum.ADMIN)) {
			log.warn("[CATEGORY BULK CREATE] Access denied: User role does not include ADMIN");
			throw new AccessDeniedException(ACCESS_DENIED_FOR_ADMIN_EXCEPTION);
		}


		log.info("[CATEGORY BULK CREATE] Starting bulk category creation. Total requested: {}", requestList.size());
		List<CreateCategoryResponseDTO> responseList = new ArrayList<>();

		for (CreateCategoryRequestDTO dto : requestList) {
			log.debug("[CATEGORY BULK CREATE] Processing category: {}", dto.getName());

			if (categoryRepository.existsByNameIgnoreCase(dto.getName())) {
				log.warn("[CATEGORY BULK CREATE] Duplicate category name found: {}", dto.getName());
				throw new DuplicateResourceException(DUPLICATE_CATEGORY_EXCEPTION);
			}

			CategoryEntity category = new CategoryEntity();
			category.setName(dto.getName());

			if (dto.getImageUrl() != null && !dto.getImageUrl().isBlank()) {
				CategoryItemImageEntity image = new CategoryItemImageEntity();
				image.setImageUrl(dto.getImageUrl());
				category.setImage(image);
				log.debug("[CATEGORY BULK CREATE] Image set for category '{}': {}", dto.getName(), dto.getImageUrl());
			}

			CategoryEntity saved = categoryRepository.save(category);
			responseList.add(CreateCategoryResponseDTO.fromEntity(saved));
		}
		log.info("[CATEGORY BULK CREATE] Successfully created {} categories", responseList.size());
		return new BulkCreateCategoryResponseDTO(responseList);
	}


	public UpdateCategoryResponseDTO updateCategory(Long id, UpdateCategoryRequestDTO dto) {
		User user = UserContextHolder.getRequiredUser();
		if (!UserContextHolder.getUser().getRoles().contains(RoleEnum.ADMIN)) {
			log.warn("[CATEGORY BULK CREATE] Access denied: User role does not include ADMIN");
			throw new AccessDeniedException(ACCESS_DENIED_FOR_ADMIN_EXCEPTION);
		}

		CategoryEntity category = categoryRepository.findById(id)
				.orElseThrow(() -> {
					log.warn("[CATEGORY UPDATE] Category not found with ID={}", id);
					return new ResourceNotFoundException(CATEGORY_NOT_FOUND, id);
				});

		log.debug("[CATEGORY UPDATE] Found category: ID={}, Current name={}", category.getId(), category.getName());

		category.setName(dto.getName());
		log.debug("[CATEGORY UPDATE] Updated name to: {}", dto.getName());

		if (dto.getImageUrl() != null && !dto.getImageUrl().isEmpty()) {
			CategoryItemImageEntity image = new CategoryItemImageEntity();
			image.setImageUrl(dto.getImageUrl());
			category.setImage(image);
			log.debug("[CATEGORY UPDATE] Updated image URL: {}", dto.getImageUrl());

		}
		CategoryEntity saved = categoryRepository.save(category);
		log.info("[CATEGORY UPDATE] Successfully updated category ID={} with new name='{}'", saved.getId(), saved.getName());

		return UpdateCategoryResponseDTO.fromEntity(saved);
	}


	public List<GetAllCategoryResponseDTO> getAllCategories() {
		log.info("Fetching all categories from the database.");
		List<CategoryEntity> categories = categoryRepository.findAll();
		if (CollectionUtils.isEmpty(categories)) {
			log.warn("No category found in DB");
			return Collections.emptyList();
		}
		log.info("Total categories found: {}", categories.size());
		return categories.stream().map(GetAllCategoryResponseDTO::fromEntity).toList();
	}

	public CreateCategoryResponseDTO getById(Long id) {
		log.info("[CATEGORY FETCH] Fetch request received for category ID={}", id);

		CategoryEntity entity = categoryRepository.findById(id)
				.orElseThrow(() -> {
					log.warn("[CATEGORY FETCH] Category not found for ID={}", id);
					return new ResourceNotFoundException(CATEGORY_NOT_FOUND, id);
				});

		log.info("[CATEGORY FETCH] Category found: ID={}, Name={}", entity.getId(), entity.getName());

		return CreateCategoryResponseDTO.fromEntity(entity);
	}

}


