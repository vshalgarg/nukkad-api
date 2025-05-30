package com.code.monks.nukkad.services;

import com.code.monks.nukkad.dto.request.CreateItemRequestDTO;
import com.code.monks.nukkad.dto.response.CreateItemResponseDTO;
import com.code.monks.nukkad.dto.response.GetAllItemResponseDTO;
import com.code.monks.nukkad.entities.CategoryEntity;
import com.code.monks.nukkad.entities.ImageEntity;
import com.code.monks.nukkad.entities.ItemEntity;
import com.code.monks.nukkad.exception.DuplicateResourceException;
import com.code.monks.nukkad.exception.ResourceNotFoundException;
import com.code.monks.nukkad.exception.UnhandledException;
import com.code.monks.nukkad.repositories.CategoryRepository;
import com.code.monks.nukkad.repositories.ItemRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.code.monks.nukkad.enums.ResponseErrorCodes.DUPLICATE_CATEGORY_EXCEPTION;
import static com.code.monks.nukkad.enums.ResponseErrorCodes.UNHANDLED_EXCEPTION;

@Service
@Slf4j
@AllArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;
	private final CategoryRepository categoryRepository;

	public CreateItemResponseDTO createItem(CreateItemRequestDTO dto) {
		try {
			log.info("Attempting to save new item: {}", dto);

			ItemEntity item = new ItemEntity();
			item.setName(dto.getName());
			item.setUnit(dto.getUnit());

			List<CategoryEntity> categories = categoryRepository.findAllById(dto.getCategoryIds());
			item.setCategories(categories);


			if (dto.getImageUrls() != null && !dto.getImageUrls().isEmpty()) {
				List<ImageEntity> images = dto.getImageUrls().stream()
						.map(url -> {
							ImageEntity image = new ImageEntity();
							image.setImageUrl(url);
							System.out.println("Url"+url);
							image.setItem(item);
							return image;
						}).collect(Collectors.toList());
				item.setImages(images);
			}



			ItemEntity saved = itemRepository.save(item);
			log.info("Item successfully saved to db: {}", saved);
			return CreateItemResponseDTO.fromEntity(saved);

		} catch (DataIntegrityViolationException e) {
			log.error("Duplicated item data or constraint violation: {}", dto, e);
			throw new DuplicateResourceException(DUPLICATE_CATEGORY_EXCEPTION);
		} catch (Exception e) {
			log.error("Unhandled exception while creating item: {}", dto, e);
			throw new UnhandledException(UNHANDLED_EXCEPTION, e);
		}
	}

	public List<GetAllItemResponseDTO> getAllItems() {
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

	public CreateItemResponseDTO getItemById(int id) {
		log.info("Fetching item by ID: {}", id);
		try {
			ItemEntity item = itemRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Item not found with ID: " + id));
			return CreateItemResponseDTO.fromEntity(item);
		} catch (ResourceNotFoundException e) {
			log.warn("Item not found with ID: {}", id);
			throw e;
		} catch (Exception e) {
			log.error("Unhandled exception while fetching item with ID: {}", id, e);
			throw new UnhandledException(UNHANDLED_EXCEPTION, e);
		}
	}

	public CreateItemResponseDTO updateItem(int id, CreateItemRequestDTO dto) {
		log.info("Attempting to update item with ID: {}", id);
		try {
			ItemEntity item = itemRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Item not found with ID: " + id));

			item.setName(dto.getName());
			item.setUnit(dto.getUnit());
			item.getImages().clear();

			if (dto.getImageUrls() != null && !dto.getImageUrls().isEmpty()) {
				List<ImageEntity> images = dto.getImageUrls().stream()
						.map(url -> {
							ImageEntity image = new ImageEntity();
							image.setImageUrl(url);
							image.setItem(item); // set back-reference
							return image;
						})
						.collect(Collectors.toList());
				item.getImages().addAll(images);
			}

			List<CategoryEntity> categories = categoryRepository.findAllById(dto.getCategoryIds());
			item.setCategories(categories);

			ItemEntity updated = itemRepository.save(item);
			log.info("Item successfully updated: {}", updated);
			return CreateItemResponseDTO.fromEntity(updated);
		} catch (ResourceNotFoundException e) {
			log.warn("Item not found during update with ID: {}", id);
			throw e;
		} catch (Exception e) {
			log.error("Unhandled exception while updating item with ID: {}", id, e);
			throw new UnhandledException(UNHANDLED_EXCEPTION, e);
		}
	}

}

