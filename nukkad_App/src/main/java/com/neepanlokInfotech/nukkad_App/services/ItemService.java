package com.neepanlokInfotech.nukkad_App.services;
import com.neepanlokInfotech.nukkad_App.dto.ItemRequestDTO;
import com.neepanlokInfotech.nukkad_App.dto.ItemResponseDTO;
import com.neepanlokInfotech.nukkad_App.entities.CategoryEntity;
import com.neepanlokInfotech.nukkad_App.entities.ItemEntity;
import com.neepanlokInfotech.nukkad_App.exception.ResourceNotFoundException;
import com.neepanlokInfotech.nukkad_App.mapper.CategoryMapper;
import com.neepanlokInfotech.nukkad_App.mapper.ItemMapper;
import com.neepanlokInfotech.nukkad_App.repositories.CategoryRepository;
import com.neepanlokInfotech.nukkad_App.repositories.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CategoryRepository categoryRepository;


 /*

 *To save item with category

  */
 public ItemResponseDTO createItem(ItemRequestDTO dto) {
     log.info("Creating new item with name: {}", dto.getName());

     ItemEntity item = ItemMapper.toEntity(dto);
     log.debug("Mapped ItemEntity: {}", item);

     List<Long> categoryIds = dto.getCategoryIds();
     log.debug("Fetching categories with IDs: {}", categoryIds);

     List<CategoryEntity> categories = categoryRepository.findAllById(categoryIds);
     item.setCategories(categories);

     ItemEntity savedItem = itemRepository.save(item);
     log.info("Item successfully saved with ID: {}", savedItem.getId());

     ItemResponseDTO responseDTO = ItemMapper.toDTO(savedItem);
     log.debug("Mapped ItemResponseDTO: {}", responseDTO);

     return responseDTO;
 }


    /*

     *get all items

     */
    public List<ItemResponseDTO> getAllItems() {
        log.info("Fetching all items from the database");

        List<ItemEntity> items = itemRepository.findAll();
        log.info("Total items fetched: {}", items.size());

        List<ItemResponseDTO> dtos = new ArrayList<>();
        for (ItemEntity item : items) {
            ItemResponseDTO dto = ItemMapper.toDTO(item);
            dtos.add(dto);
            log.debug("Mapped ItemEntity to DTO: {}", dto); // Optional: keep at debug level
        }

        return dtos;
    }


    /*

    *get item by id

     */
    public ItemResponseDTO getItemById(Long id) {
        log.info("Fetching item with ID: {}", id);

        ItemEntity item = itemRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Item not found with ID: {}", id);
                    return new ResourceNotFoundException("Item not found with ID: " + id);
                });

        ItemResponseDTO dto = ItemMapper.toDTO(item);
        log.info("Successfully fetched and mapped item with ID: {}", id);
        return dto;
    }


    /*

     *Update Item

     */
    public ItemResponseDTO updateItem(Long id, ItemRequestDTO dto) {
        log.info("Request to update item with ID: {}", id);

        // Fetching the existing item
        ItemEntity item = itemRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Item not found with ID: {}", id);
                    return new ResourceNotFoundException("Item not found with ID: " + id);
                });

        log.info("Item found with ID: {}", id);

        // Updating item details
        item.setName(dto.getName());
        item.setImage(dto.getImage());
        item.setUnit(dto.getUnit());

        // Setting categories
        List<CategoryEntity> categories = categoryRepository.findAllById(dto.getCategoryIds());
        item.setCategories(categories);
        log.info("Categories associated with item ID {}: {}", id, dto.getCategoryIds());

        // Saving the updated item
        ItemEntity updated = itemRepository.save(item);
        log.info("Item updated successfully with ID: {}", updated.getId());

        // Returning updated DTO
        return ItemMapper.toDTO(updated);
    }


    /*
    *Delete Items By id

    */
//    public String deleteItem(Long id) {
//        if (!itemRepository.existsById(id)) {
//            throw new RuntimeException("Item not found");
//        }
//        itemRepository.deleteById(id);
//        return "Item got deleted";
//    }
}
