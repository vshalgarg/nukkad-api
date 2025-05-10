package com.neepanlokInfotech.nukkad_App.services;
import com.neepanlokInfotech.nukkad_App.dto.ItemRequestDTO;
import com.neepanlokInfotech.nukkad_App.dto.ItemResponseDTO;
import com.neepanlokInfotech.nukkad_App.entities.Category;
import com.neepanlokInfotech.nukkad_App.entities.Item;
import com.neepanlokInfotech.nukkad_App.mapper.ItemMapper;
import com.neepanlokInfotech.nukkad_App.repositories.CategoryRepository;
import com.neepanlokInfotech.nukkad_App.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CategoryRepository categoryRepository;


 // to save item with category
    public ItemResponseDTO createItem(ItemRequestDTO dto) {
        Item item = ItemMapper.toEntity(dto);
        List<Category> categories = categoryRepository.findAllById(dto.getCategoryIds());
        item.setCategories(categories);
        Item saved = itemRepository.save(item);
        return ItemMapper.toDTO(saved);
    }

    // get all items
    public List<ItemResponseDTO> getAllItems() {
        List<Item> items = itemRepository.findAll();
        List<ItemResponseDTO> dtos = new ArrayList<>();

        for (Item item : items) {
            ItemResponseDTO dto = ItemMapper.toDTO(item);
            dtos.add(dto);
        }
        return dtos;
    }

    //get item by id
    public ItemResponseDTO getItemById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        return ItemMapper.toDTO(item);
    }

    // upadte Item
    public ItemResponseDTO updateItem(Long id, ItemRequestDTO dto) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        item.setItemName(dto.getItemName());
        item.setImage(dto.getImage());
        item.setUnit(dto.getUnit());

        List<Category> categories = categoryRepository.findAllById(dto.getCategoryIds());
        item.setCategories(categories);

        Item updated = itemRepository.save(item);
        return ItemMapper.toDTO(updated);
    }

//   // delete item
//    public String deleteItem(Long id) {
//        if (!itemRepository.existsById(id)) {
//            throw new RuntimeException("Item not found");
//        }
//        itemRepository.deleteById(id);
//        return "Item got deleted";
//    }
}
