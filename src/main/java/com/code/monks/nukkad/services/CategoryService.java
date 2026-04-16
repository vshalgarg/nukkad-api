package com.code.monks.nukkad.services;

import com.code.monks.nukkad.constants.FirebaseConstants;
import com.code.monks.nukkad.context.UserContextHolder;
import com.code.monks.nukkad.dto.User;
import com.code.monks.nukkad.dto.category.CategoryDto;
import com.code.monks.nukkad.dto.category.PaginatedCategoryResponse;
import com.code.monks.nukkad.dto.request.CreateCategoryRequestDTO;
import com.code.monks.nukkad.dto.request.UpdateCategoryRequestDTO;
import com.code.monks.nukkad.dto.response.BulkCreateCategoryResponseDTO;
import com.code.monks.nukkad.dto.response.CreateCategoryResponseDTO;
import com.code.monks.nukkad.dto.response.GetAllCategoryResponseDTO;
import com.code.monks.nukkad.dto.response.UpdateCategoryResponseDTO;
import com.code.monks.nukkad.entities.CategoryEntity;
import com.code.monks.nukkad.entities.CategoryItemImageEntity;
import com.code.monks.nukkad.entities.ItemEntity;
import com.code.monks.nukkad.repositories.ItemRepository;
import com.code.monks.nukkad.enums.RoleEnum;
import com.code.monks.nukkad.exception.AccessDeniedException;
import com.code.monks.nukkad.exception.DuplicateResourceException;
import com.code.monks.nukkad.exception.ResourceNotFoundException;
import com.code.monks.nukkad.exception.UnhandledException;
import com.code.monks.nukkad.repositories.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    private final FirebaseStorageService firebaseStorageService; // ← add

    private final ItemRepository itemRepository;

	private final CategoryRepository categoryRepository;

	public BulkCreateCategoryResponseDTO createBulkCategories(List<CreateCategoryRequestDTO> requestList) {
		User admin = UserContextHolder.getRequiredUser();

		if (!admin.getRoles().contains(RoleEnum.ADMIN)) {
			log.warn("[CATEGORY BULK CREATE] Access denied: User role does not include ADMIN");
			throw new AccessDeniedException(ACCESS_DENIED_FOR_ADMIN_EXCEPTION);
		}

		log.info("[CATEGORY BULK CREATE] Starting bulk creation. Total requested: {}", requestList.size());

		List<CreateCategoryResponseDTO> responseList = new ArrayList<>();

		try {
			for (CreateCategoryRequestDTO dto : requestList) {
				log.debug("[CATEGORY BULK CREATE] Processing category: {}", dto.getName());

				if (categoryRepository.existsByNameIgnoreCase(dto.getName())) {
					log.warn("[CATEGORY BULK CREATE] Duplicate category name found: {}", dto.getName());
					throw new DuplicateResourceException(DUPLICATE_CATEGORY_EXCEPTION, dto.getName());
				}

				CategoryEntity category = new CategoryEntity();
				category.setName(dto.getName());

				if (dto.getImageUrl() != null && !dto.getImageUrl().isBlank()) {

                    String firebaseUrl = firebaseStorageService
                            .uploadImageFromUrl(dto.getImageUrl(), FirebaseConstants.CATEGORY_IMAGE_TYPE);//folder/categories/category_123456.png ✅

					CategoryItemImageEntity image = new CategoryItemImageEntity();

                    image.setImageUrl(firebaseUrl);
					category.setImage(image);

                    log.debug("[CATEGORY UPDATE] Image uploaded to Firebase: {}", firebaseUrl);
                }

				CategoryEntity saved = categoryRepository.save(category);
				responseList.add(CreateCategoryResponseDTO.fromEntity(saved));

				log.info("[CATEGORY BULK CREATE] Successfully created category: '{}'", saved.getName());
			}

			log.info("[CATEGORY BULK CREATE] Successfully created all {} categories.", responseList.size());
			return new BulkCreateCategoryResponseDTO(responseList);

		} catch (DuplicateResourceException e) {
			log.error("[CATEGORY BULK CREATE] Duplicate category error: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("[CATEGORY BULK CREATE] Unexpected error during bulk creation", e);
			throw new UnhandledException(UNHANDLED_EXCEPTION,e);
		}
	}



	public UpdateCategoryResponseDTO updateCategory(Long id, UpdateCategoryRequestDTO dto) {
		User user = UserContextHolder.getRequiredUser();

		if (!user.getRoles().contains(RoleEnum.ADMIN)) {
			log.warn("[CATEGORY UPDATE] Access denied for userId={}. Role does not include ADMIN", user.getId());
			throw new AccessDeniedException(ACCESS_DENIED_FOR_ADMIN_EXCEPTION);
		}

		log.info("[CATEGORY UPDATE] Request received to update categoryId={} by adminId={}", id, user.getId());

		CategoryEntity category = categoryRepository.findById(id)
				.orElseThrow(() -> {
					log.warn("[CATEGORY UPDATE] Category not found. ID={}", id);
					return new ResourceNotFoundException(CATEGORY_NOT_FOUND, id);
				});

		log.debug("[CATEGORY UPDATE] Existing category found: ID={}, Name='{}'", category.getId(), category.getName());

		try {
			// Update name
			category.setName(dto.getName());
			log.debug("[CATEGORY UPDATE] Category name updated to '{}'", dto.getName());

			// Update image if present
			if (dto.getImageUrl() != null && !dto.getImageUrl().isBlank()) {

                String firebaseUrl = firebaseStorageService
                        .uploadImageFromUrl(dto.getImageUrl(),FirebaseConstants.CATEGORY_IMAGE_TYPE);

				CategoryItemImageEntity image = new CategoryItemImageEntity();
				image.setImageUrl(firebaseUrl);
				category.setImage(image);
//
                log.debug("[CATEGORY BULK CREATE] Image uploaded to Firebase for '{}': {}",
                        dto.getName(), firebaseUrl);
            }

			CategoryEntity saved = categoryRepository.save(category);
			log.info("[CATEGORY UPDATE] Category successfully updated. ID={}, Name='{}'", saved.getId(), saved.getName());

			return UpdateCategoryResponseDTO.fromEntity(saved);

		} catch (Exception e) {
			log.error("[CATEGORY UPDATE] Failed to update categoryId={}. Error: {}", id, e.getMessage(), e);
			throw new UnhandledException(UNHANDLED_EXCEPTION,e);
		}
	}



	public List<GetAllCategoryResponseDTO> getAllCategories() {
		log.info("[CATEGORY FETCH ALL] Fetching all categories from the database");

		try {
			List<CategoryEntity> categories = categoryRepository.findAll();

			if (CollectionUtils.isEmpty(categories)) {
				log.warn("[CATEGORY FETCH ALL] No categories found in the database.");
				return Collections.emptyList();
			}

			log.info("[CATEGORY FETCH ALL] Total categories found: {}", categories.size());
			return categories.stream()
					.map(GetAllCategoryResponseDTO::fromEntity)
					.toList();

		} catch (Exception e) {
			log.error("[CATEGORY FETCH ALL] Unexpected error occurred while fetching categories: {}", e.getMessage(), e);
			throw new UnhandledException(UNHANDLED_EXCEPTION,e);
		}
	}

	public CreateCategoryResponseDTO getById(Long id) {
		log.info("[CATEGORY FETCH] Fetch request received for category ID={}", id);

		try {
			CategoryEntity entity = categoryRepository.findById(id)
					.orElseThrow(() -> {
						log.warn("[CATEGORY FETCH] Category not found for ID={}", id);
						return new ResourceNotFoundException(CATEGORY_NOT_FOUND, id);
					});

			log.info("[CATEGORY FETCH] Category found: ID={}, Name={}", entity.getId(), entity.getName());
			return CreateCategoryResponseDTO.fromEntity(entity);

		} catch (ResourceNotFoundException e) {
			log.error("[CATEGORY FETCH] Category not found: {}", e.getMessage());
			throw e;

		} catch (Exception e) {
			log.error("[CATEGORY FETCH] Unexpected error occurred while fetching category ID={}: {}", id, e.getMessage(), e);
			throw new UnhandledException(UNHANDLED_EXCEPTION,e);
		}
	}

    @Transactional
    public void deleteById(Long id) {

        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Category not found for ID={}", id);
                    return new ResourceNotFoundException(CATEGORY_NOT_FOUND, id);
                });

            //1. We DON'T delete the items.
            // 2. We just delete the category.
            // Because of ManyToMany, JPA will automatically delete the rows in
            // the JOIN TABLE (the links), but keep the items safe.
        categoryRepository.delete(category);

        log.info("[DELETE CATEGORY REQUEST SUCCESS] deleted category with ID: {}", id);
    }

	public PaginatedCategoryResponse getAllCategories(int page, int size) {
		log.info("Fetching categories - Page: {}, Size: {}", page, size);

		Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
		Page<CategoryEntity> categoryPage = categoryRepository.findAll(pageable);

		List<CategoryDto> categoryDtos = categoryPage.getContent().stream()
				.map(CategoryDto::fromEntity)
				.toList();

		log.info("Found {} categories on page {}", categoryDtos.size(), page);

		PaginatedCategoryResponse response = new PaginatedCategoryResponse();
		response.setContent(categoryDtos);
		response.setTotalPages(categoryPage.getTotalPages());
		response.setTotalElements(categoryPage.getTotalElements());
		response.setNumber(categoryPage.getNumber());
		response.setSize(categoryPage.getSize());

		log.debug("Returning Paginated Response: totalPages={}, totalElements={}, currentPage={}",
				response.getTotalPages(), response.getTotalElements(), response.getNumber());

		return response;
	}
}


