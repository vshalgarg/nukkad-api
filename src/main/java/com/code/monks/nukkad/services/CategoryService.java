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

		List<CreateCategoryResponseDTO> responseList = new ArrayList<>();
		for (CreateCategoryRequestDTO dto : requestList) {
			if (categoryRepository.existsByNameIgnoreCase(dto.getName())) {
				throw new DuplicateResourceException(DUPLICATE_CATEGORY_EXCEPTION);
			}

			CategoryEntity category = new CategoryEntity();
			category.setName(dto.getName());

			if (dto.getImageUrl() != null && !dto.getImageUrl().isBlank()) {
				CategoryItemImageEntity image = new CategoryItemImageEntity();
				image.setImageUrl(dto.getImageUrl());
				category.setImage(image);
			}

			CategoryEntity saved = categoryRepository.save(category);
			responseList.add(CreateCategoryResponseDTO.fromEntity(saved));
		}

		return new BulkCreateCategoryResponseDTO(responseList);
	}


	public UpdateCategoryResponseDTO updateCategory(Long id, UpdateCategoryRequestDTO dto) {
		User user = UserContextHolder.getRequiredUser();
		if (!UserContextHolder.getUser().getRoles().contains(RoleEnum.ADMIN)) {
			log.warn("[CATEGORY BULK CREATE] Access denied: User role does not include ADMIN");
			throw new AccessDeniedException(ACCESS_DENIED_FOR_ADMIN_EXCEPTION);
		}

		CategoryEntity category = categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(CATEGORY_NOT_FOUND, id));

		category.setName(dto.getName());

		if (dto.getImageUrl() != null && !dto.getImageUrl().isEmpty()) {
			CategoryItemImageEntity image = new CategoryItemImageEntity();
			image.setImageUrl(dto.getImageUrl());
			category.setImage(image);
		}

		return UpdateCategoryResponseDTO.fromEntity(categoryRepository.save(category));
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
		CategoryEntity entity = categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(CATEGORY_NOT_FOUND, id));
		return CreateCategoryResponseDTO.fromEntity(entity);

	}
}


