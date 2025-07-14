package com.code.monks.nukkad.services;

import com.code.monks.nukkad.context.UserContextHolder;
import com.code.monks.nukkad.dto.User;
import com.code.monks.nukkad.dto.request.CreateItemRequestDTO;
import com.code.monks.nukkad.dto.request.UpdateItemRequestDTO;
import com.code.monks.nukkad.dto.response.*;
import com.code.monks.nukkad.entities.CategoryEntity;
import com.code.monks.nukkad.entities.CategoryItemImageEntity;
import com.code.monks.nukkad.entities.ItemEntity;
import com.code.monks.nukkad.enums.RoleEnum;
import com.code.monks.nukkad.exception.AccessDeniedException;
import com.code.monks.nukkad.exception.ResourceNotFoundException;
import com.code.monks.nukkad.exception.UnhandledException;
import com.code.monks.nukkad.repositories.CategoryRepository;
import com.code.monks.nukkad.repositories.ItemRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.code.monks.nukkad.enums.ResponseErrorCodes.*;

@Service
@Slf4j
@AllArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;
	private final CategoryRepository categoryRepository;

	public BulkCreateItemResponseDTO createBulkItems(List<CreateItemRequestDTO> requestList) {
		User admin = UserContextHolder.getRequiredUser();
		if (!admin.getRoles().contains(RoleEnum.ADMIN)) {
			log.warn("[ITEM BULK CREATE] Access denied: User role does not include ADMIN (userId={})", admin.getId());
			throw new AccessDeniedException(ACCESS_DENIED_FOR_ADMIN_EXCEPTION);
		}

		log.info("[ITEM BULK CREATE] Starting bulk item creation. Total items to process: {}", requestList.size());

		List<CreateItemResponseDTO> responses = new ArrayList<>();

		for (CreateItemRequestDTO dto : requestList) {
			log.debug("[ITEM BULK CREATE] Processing item '{}'", dto.getName());

			try {

				if (dto.getCategoryIds() == null || dto.getCategoryIds().isEmpty()) {
					log.warn("[ITEM BULK CREATE] Item '{}' skipped - No category IDs provided", dto.getName());
					throw new IllegalArgumentException("Item must be associated with at least one category.");
				}


				List<CategoryEntity> categories = categoryRepository.findAllById(dto.getCategoryIds());
				if (categories.size() != dto.getCategoryIds().size()) {
					log.error("[ITEM BULK CREATE] Item '{}' has invalid/missing categories. Expected={}, Found={}",
							dto.getName(), dto.getCategoryIds().size(), categories.size());
					throw new ResourceNotFoundException(CATEGORY_NOT_FOUND_TO_SAVE_ITEM_EXCEPTION);
				}


				ItemEntity item = new ItemEntity();
				item.setName(dto.getName());
				item.setUnit(dto.getUnit());
				item.setCategories(categories);
				log.debug("[ITEM BULK CREATE] Basic fields and categories set for item '{}'", dto.getName());


				if (dto.getImageUrls() != null && !dto.getImageUrls().isEmpty()) {
					List<CategoryItemImageEntity> images = dto.getImageUrls().stream()
							.map(url -> {
								CategoryItemImageEntity image = new CategoryItemImageEntity();
								image.setImageUrl(url);
								image.setItem(item);
								return image;
							})
							.toList();
					item.setImages(images);
					log.debug("[ITEM BULK CREATE] Attached {} image(s) to item '{}'", images.size(), dto.getName());
				}


				ItemEntity saved = itemRepository.save(item);
				log.info("[ITEM BULK CREATE] Item saved successfully: ID={}, Name='{}'", saved.getId(), saved.getName());
				responses.add(CreateItemResponseDTO.fromEntity(saved));

			} catch (IllegalArgumentException | ResourceNotFoundException ex) {
				log.error("[ITEM BULK CREATE] Error for item '{}': {}", dto.getName(), ex.getMessage());
				throw ex;
			} catch (Exception e) {
				log.error("[ITEM BULK CREATE] Unexpected error while processing item '{}': {}", dto.getName(), e.getMessage(), e);
				throw new UnhandledException(UNHANDLED_EXCEPTION,e);
			}
		}

		log.info("[ITEM BULK CREATE] Successfully created {} item(s)", responses.size());
		return new BulkCreateItemResponseDTO(responses);
	}



	public List<GetAllItemResponseDTO> getAllItems() {
		User admin = UserContextHolder.getRequiredUser();
		if (!admin.getRoles().contains(RoleEnum.ADMIN)) {
			log.warn("[ITEM FETCH ALL] Access denied: User role does not include ADMIN (userId={})", admin.getId());
			throw new AccessDeniedException(ACCESS_DENIED_FOR_ADMIN_EXCEPTION);
		}

		log.info("[ITEM FETCH ALL] Fetching all items from the database");

		try {
			List<ItemEntity> items = itemRepository.findAll();

			if (items == null || items.isEmpty()) {
				log.warn("[ITEM FETCH ALL] No items found in the database.");
				return Collections.emptyList();
			}

			log.info("[ITEM FETCH ALL] Found {} item(s)", items.size());

			return items.stream()
					.map(GetAllItemResponseDTO::fromEntity)
					.collect(Collectors.toList());

		} catch (Exception e) {
			log.error("[ITEM FETCH ALL] Unexpected error while fetching items", e);
			throw new UnhandledException(UNHANDLED_EXCEPTION, e);
		}
	}


	public CreateItemResponseDTO getItemById(Long id) {
		User admin = UserContextHolder.getRequiredUser();
		if (!admin.getRoles().contains(RoleEnum.ADMIN)) {
			log.warn("[ITEM UPDATE] Access denied: User role does not include ADMIN");
			throw new AccessDeniedException(ACCESS_DENIED_FOR_ADMIN_EXCEPTION);
		}

		log.info("Fetching item by ID: {}", id);
		try {
			ItemEntity item = itemRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException(ITEM_NOT_FOUND, (long) id));
			return CreateItemResponseDTO.fromEntity(item);
		} catch (ResourceNotFoundException e) {
			log.warn("Item not found with ID: {}", id);
			throw e;
		} catch (Exception e) {
			log.error("Unhandled exception while fetching item with ID: {}", id, e);
			throw new UnhandledException(UNHANDLED_EXCEPTION,e);
		}
	}

	public UpdateItemResponseDTO updateItem(Long id, UpdateItemRequestDTO dto) {
		User admin = UserContextHolder.getRequiredUser();
		if (!admin.getRoles().contains(RoleEnum.ADMIN)) {
			log.warn("[ITEM UPDATE] Access denied: User role does not include ADMIN");
			throw new AccessDeniedException(ACCESS_DENIED_FOR_ADMIN_EXCEPTION);
		}

		log.info("Attempting to update item with ID: {}", id);
		try {
			ItemEntity item = itemRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException(ITEM_NOT_FOUND));

			item.setName(dto.getName());
			item.setUnit(dto.getUnit());
			item.getImages().clear();

			if (dto.getImageUrls() != null && !dto.getImageUrls().isEmpty()) {
				List<CategoryItemImageEntity> images = dto.getImageUrls().stream()
						.map(url -> {
							CategoryItemImageEntity image = new CategoryItemImageEntity();
							image.setImageUrl(url);
							image.setItem(item);
							return image;
						})
						.toList();
				item.getImages().addAll(images);
			}

			List<CategoryEntity> categories = categoryRepository.findAllById(dto.getCategoryIds());
			item.setCategories(categories);

			ItemEntity updated = itemRepository.save(item);
			log.info("Item successfully updated: {}", updated);
			return UpdateItemResponseDTO.fromEntity(updated);
		} catch (ResourceNotFoundException e) {
			log.warn("Item not found during update with ID: {}", id);
			throw e;
		} catch (Exception e) {
			log.error("Unhandled exception while updating item with ID: {}", id, e);
			throw new UnhandledException(UNHANDLED_EXCEPTION, e);
		}
	}

	public GetItemsByCategoryResponseDTO getItemsByCategory(Long categoryId) {
		User user = UserContextHolder.getRequiredUser();

		log.info("[ITEM FETCH BY CATEGORY] Request received by userId={} for categoryId={}", user.getId(), categoryId);

		if (!user.getRoles().contains(RoleEnum.CUSTOMER)) {
			log.warn("[ITEM FETCH BY CATEGORY] Access denied: Role '{}' is not authorized", user.getRoles());
			throw new AccessDeniedException(ACCESS_DENIED_FOR_STOREKEEPER_EXCEPTION);
		}

		try {
			CategoryEntity category = categoryRepository.findById(categoryId)
					.orElseThrow(() -> {
						log.warn("[ITEM FETCH BY CATEGORY] Category not found for ID={}", categoryId);
						return new ResourceNotFoundException(CATEGORY_NOT_FOUND, categoryId);
					});

			List<ItemEntity> items = category.getItems();
			if (items == null || items.isEmpty()) {
				log.warn("[ITEM FETCH BY CATEGORY] No items found for categoryId={}", categoryId);
				return GetItemsByCategoryResponseDTO.builder()
						.message("No items found for this category")
						.items(Collections.emptyList())
						.build();
			}

			List<GetAllItemResponseDTO> dtoList = items.stream()
					.map(GetAllItemResponseDTO::fromEntity)
					.collect(Collectors.toList());

			log.info("[ITEM FETCH BY CATEGORY] {} item(s) found for categoryId={}", dtoList.size(), categoryId);

			return GetItemsByCategoryResponseDTO.builder()
					.message("Items fetched successfully for category")
					.items(dtoList)
					.build();

		} catch (ResourceNotFoundException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("[ITEM FETCH BY CATEGORY] Unexpected error while fetching items for categoryId={}", categoryId, e);
			throw new UnhandledException(UNHANDLED_EXCEPTION, e);
		}
	}

}

