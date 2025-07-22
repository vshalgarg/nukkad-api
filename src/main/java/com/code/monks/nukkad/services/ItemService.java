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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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



	public Page<GetAllItemResponseDTO> getAllItems(int page, int size, String sortBy) {
		User admin = UserContextHolder.getRequiredUser();
		if (!admin.getRoles().contains(RoleEnum.ADMIN)) {
			log.warn("[ITEM FETCH ALL] Access denied: User role does not include ADMIN (userId={})", admin.getId());
			throw new AccessDeniedException(ACCESS_DENIED_FOR_ADMIN_EXCEPTION);
		}

		log.info("[ITEM FETCH ALL] Fetching items from DB with pagination - page: {}, size: {}, sortBy: {}", page, size, sortBy);

		try {
			Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
			Page<ItemEntity> itemPage = itemRepository.findAll(pageable);

			if (itemPage.isEmpty()) {
				log.warn("[ITEM FETCH ALL] No items found in DB.");
				return Page.empty();
			}

			log.info("[ITEM FETCH ALL] Found {} item(s)", itemPage.getTotalElements());

			return itemPage.map(GetAllItemResponseDTO::fromEntity);

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

	public GetItemsByCategoryResponseDTO getItemsByCategory(Long categoryId, Pageable pageable) {
		User user = UserContextHolder.getRequiredUser();
		log.info("[ITEM FETCH BY CATEGORY] Request received by userId={} for categoryId={}", user.getId(), categoryId);

		try {
			// Check if user is CUSTOMER
			if (!user.getRoles().contains(RoleEnum.CUSTOMER)) {
				log.warn("[ACCESS DENIED] UserId={} with roles={} is not allowed to access items by category",
						user.getId(), user.getRoles());
				throw new AccessDeniedException(ACCESS_DENIED_FOR_STOREKEEPER_EXCEPTION);
			}

			// Validate category
			categoryRepository.findById(categoryId)
					.orElseThrow(() -> {
						log.warn("[CATEGORY NOT FOUND] categoryId={} not found", categoryId);
						return new ResourceNotFoundException(CATEGORY_NOT_FOUND, categoryId);
					});

			// Fetch paginated items
			Page<ItemEntity> itemsPage = itemRepository.findItemsByCategoryId(categoryId, pageable);

			List<GetAllItemResponseDTO> dtoList = itemsPage.getContent().stream()
					.map(GetAllItemResponseDTO::fromEntity)
					.collect(Collectors.toList());

			log.info("[ITEMS FETCHED SUCCESSFULLY] categoryId={}, totalItems={}, pageNumber={}, pageSize={}",
					categoryId, dtoList.size(), itemsPage.getNumber(), itemsPage.getSize());

			return GetItemsByCategoryResponseDTO.builder()
					.message("Items fetched successfully for category")
					.items(dtoList)
					.build();

		} catch (AccessDeniedException | ResourceNotFoundException ex) {
			throw ex;
		} catch (Exception ex) {
			log.error("[ITEM FETCH ERROR] categoryId={}, error={}", categoryId, ex.getMessage(), ex);
			throw new UnhandledException(UNHANDLED_EXCEPTION, ex);
		}
	}


}

