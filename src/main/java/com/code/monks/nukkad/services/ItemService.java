package com.code.monks.nukkad.services;

import com.code.monks.nukkad.context.UserContextHolder;
import com.code.monks.nukkad.dto.User;
import com.code.monks.nukkad.dto.request.CreateItemRequestDTO;
import com.code.monks.nukkad.dto.request.UpdateItemRequestDTO;
import com.code.monks.nukkad.dto.response.BulkCreateItemResponseDTO;
import com.code.monks.nukkad.dto.response.CreateItemResponseDTO;
import com.code.monks.nukkad.dto.response.GetAllItemResponseDTO;
import com.code.monks.nukkad.dto.response.UpdateItemResponseDTO;
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
			log.warn("[ITEM BULK CREATE] Access denied: User role does not include ADMIN");
			throw new AccessDeniedException(ACCESS_DENIED_FOR_ADMIN_EXCEPTION);
		}

		List<CreateItemResponseDTO> responses = new ArrayList<>();

		for (CreateItemRequestDTO dto : requestList) {
			if (dto.getCategoryIds() == null || dto.getCategoryIds().isEmpty()) {
				throw new IllegalArgumentException("Item must be associated with at least one category.");
			}

			ItemEntity item = new ItemEntity();
			item.setName(dto.getName());
			item.setUnit(dto.getUnit());

			List<CategoryEntity> categories = categoryRepository.findAllById(dto.getCategoryIds());
			if (categories.size() != dto.getCategoryIds().size()) {
				throw new ResourceNotFoundException(ITEM_NOT_SAVED_EXCEPTION);
			}
			item.setCategories(categories);

			if (dto.getImageUrls() != null && !dto.getImageUrls().isEmpty()) {
				List<CategoryItemImageEntity> images = dto.getImageUrls().stream()
						.map(url -> {
							CategoryItemImageEntity image = new CategoryItemImageEntity();
							image.setImageUrl(url);
							image.setItem(item);
							return image;
						}).toList();
				item.setImages(images);
			}

			ItemEntity saved = itemRepository.save(item);
			responses.add(CreateItemResponseDTO.fromEntity(saved));
		}

		return new BulkCreateItemResponseDTO(responses);
	}


	public List<GetAllItemResponseDTO> getAllItems() {

		User admin = UserContextHolder.getRequiredUser();
		if (!admin.getRoles().contains(RoleEnum.ADMIN)) {
			log.warn("[ITEM UPDATE] Access denied: User role does not include ADMIN");
			throw new AccessDeniedException(ACCESS_DENIED_FOR_ADMIN_EXCEPTION);
		}

		log.info("Fetching all items");
		try {
			List<ItemEntity> items = itemRepository.findAll();
			if (items.isEmpty()) {
				log.warn("No items found in database");
				return Collections.emptyList();
			}
			return items.stream()
					.map(GetAllItemResponseDTO::fromEntity)
					.collect(Collectors.toList());
		} catch (Exception e) {
			log.error("Unhandled exception while fetching all items", e);
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

}

