package com.code.monks.nukkad.services;

import com.code.monks.nukkad.context.UserContextHolder;
import com.code.monks.nukkad.dto.User;
import com.code.monks.nukkad.dto.request.CreateItemRequestDTO;
import com.code.monks.nukkad.dto.request.UpdateItemRequestDTO;
import com.code.monks.nukkad.dto.response.*;
import com.code.monks.nukkad.entities.CategoryEntity;
import com.code.monks.nukkad.entities.CategoryItemImageEntity;
import com.code.monks.nukkad.entities.ItemEntity;
import com.code.monks.nukkad.enums.ImageUploadStatusEnum;
import com.code.monks.nukkad.enums.RoleEnum;
import com.code.monks.nukkad.exception.AccessDeniedException;
import com.code.monks.nukkad.exception.DuplicateResourceException;
import com.code.monks.nukkad.exception.ResourceNotFoundException;
import com.code.monks.nukkad.exception.UnhandledException;
import com.code.monks.nukkad.repositories.CategoryRepository;
import com.code.monks.nukkad.repositories.ItemRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
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

        private void requireAdminRole(String context) {
        User user = UserContextHolder.getRequiredUser();
        if (!user.getRoles().contains(RoleEnum.ADMIN)) {
            log.warn("[{}] Access denied: User is not ADMIN (userId={})", context, user.getId());
            throw new AccessDeniedException(ACCESS_DENIED_FOR_ADMIN_EXCEPTION);
        }
    }
    private CategoryItemImageEntity buildImageEntity(String url, ItemEntity item) {
        CategoryItemImageEntity image = new CategoryItemImageEntity();
        image.setImageUrl(url);
        image.setItem(item);
        image.setUploadStatus(ImageUploadStatusEnum.PENDING);
        image.setRetryCount(0);
        return image;
    }
//service methods
	public BulkCreateItemResponseDTO createBulkItems(List<CreateItemRequestDTO> requestList) {
        requireAdminRole("ITEM BULK CREATE");

        log.info("[ITEM BULK CREATE] Starting bulk item creation. Total items to process: {}", requestList.size());

		List<CreateItemResponseDTO> responses = new ArrayList<>();

		for (CreateItemRequestDTO dto : requestList) {
			log.debug("[ITEM BULK CREATE] Processing item '{}'", dto.getName());

			try {

                if (itemRepository.existsByName(dto.getName()))
                {
                    log.warn("[ITEM BULK CREATE] Duplicate item found in DB: {}", dto.getName());
                    continue;
                }
				if (dto.getCategoryIds() == null || dto.getCategoryIds().isEmpty())
                {
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
                            .map(url -> buildImageEntity(url, item))
                            .toList();
					item.setImages(images);
					log.debug("[ITEM BULK CREATE] Attached {} image(s) to item '{}'", images.size(), dto.getName());
				}


				ItemEntity saved = itemRepository.save(item);
				log.info("[ITEM BULK CREATE] Item saved successfully: ID={}, Name='{}'", saved.getId(), saved.getName());
				responses.add(CreateItemResponseDTO.fromEntity(saved));

			} catch (ResourceNotFoundException ex) {
				log.error("[ITEM BULK CREATE] CATEGORY_NOT_FOUND_TO_SAVE_ITEM_EXCEPTION '{}': {}", dto.getName(), ex.getMessage());
				throw new ResourceNotFoundException(CATEGORY_NOT_FOUND_TO_SAVE_ITEM_EXCEPTION);
			}
			catch (DataIntegrityViolationException ex) {
				log.warn("[ITEM BULK CREATE] Duplicate item detected: {}", dto.getName());
				throw new DuplicateResourceException(DUPLICATE_PRODUCT_FOUND);
			}
        }
            log.info("[ITEM BULK CREATE] Successfully created {} item(s)", responses.size());
            return new BulkCreateItemResponseDTO(responses);
	}



	public Page<GetAllItemResponseDTO> getAllItems(int page, int size, String sortBy) {
        requireAdminRole("ITEM FETCH ALL");
		log.info("[ITEM FETCH ALL] Fetching items from DB with pagination - page: {}, size: {}, sortBy: {}", page, size, sortBy);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        Page<ItemEntity> itemPage = itemRepository.findAll(pageable);

        if (itemPage.isEmpty()) {
            log.warn("[ITEM FETCH ALL] No items found in DB.");
            return Page.empty();
        }
        log.info("[ITEM FETCH ALL] Found {} item(s)", itemPage.getTotalElements());
        return itemPage.map(GetAllItemResponseDTO::fromEntity);
    }

	public CreateItemResponseDTO getItemById(Long id) {

        requireAdminRole("ITEM FETCH BY ID");
		log.info("Fetching item by ID: {}", id);

        ItemEntity item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ITEM_NOT_FOUND, id));
        return CreateItemResponseDTO.fromEntity(item);
	}

	public UpdateItemResponseDTO updateItem(Long id, UpdateItemRequestDTO dto) {

		log.info("Attempting to update item with ID: {}", id);
        ItemEntity item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ITEM_NOT_FOUND));

        item.setName(dto.getName());
        item.setUnit(dto.getUnit());

        if (dto.getImageUrls() != null) {
            item.getImages().clear();
            List<CategoryItemImageEntity> images = dto.getImageUrls().stream()
                    .map(url -> buildImageEntity(url, item))
                    .toList();

            item.setImages(images);
        }
        List<CategoryEntity> categories = categoryRepository.findAllById(dto.getCategoryIds());
        item.setCategories(categories);
        ItemEntity updated = itemRepository.save(item);
        log.info("Item successfully updated: {}", updated);
        return UpdateItemResponseDTO.fromEntity(updated);
    }

	public GetItemsByCategoryResponseDTO getItemsByCategory(Long categoryId, Pageable pageable) {
		User user = UserContextHolder.getRequiredUser();
		log.info("[ITEM FETCH BY CATEGORY] Request received by userId={} for categoryId={}", user.getId(), categoryId);

		try {categoryRepository.findById(categoryId)
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
					.totalItems(itemsPage.getTotalElements())
					.totalPages(itemsPage.getTotalPages())
					.currentPage(itemsPage.getNumber())
					.pageSize(itemsPage.getSize())
					.build();

		} catch (AccessDeniedException | ResourceNotFoundException ex) {
			throw ex;
		} catch (Exception ex) {
			log.error("[ITEM FETCH ERROR] categoryId={}, error={}", categoryId, ex.getMessage(), ex);
			throw new UnhandledException(UNHANDLED_EXCEPTION, ex);
		}
	}

	public void deleteItem(Long id) {
        requireAdminRole("ITEM DELETE");

        ItemEntity item = itemRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("[ITEM NOT FOUND] itemId={} not found", id);
                    return new ResourceNotFoundException(ITEM_NOT_FOUND, id);
                });
		itemRepository.delete(item);
		log.info("[DELETE ITEM REQUEST SUCCESS] deleted item with ID: {}", id);
	}
}

