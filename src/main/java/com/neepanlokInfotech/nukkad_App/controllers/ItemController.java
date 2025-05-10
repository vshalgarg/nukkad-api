package com.neepanlokInfotech.nukkad_App.controllers;


import com.neepanlokInfotech.nukkad_App.dto.ItemRequestDTO;
import com.neepanlokInfotech.nukkad_App.dto.ItemResponseDTO;
import com.neepanlokInfotech.nukkad_App.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    // Create new item
    @PostMapping
    public ResponseEntity<ItemResponseDTO> createItem(@RequestBody ItemRequestDTO dto) {
        ItemResponseDTO savedItem = itemService.createItem(dto);
        return ResponseEntity.ok(savedItem);
    }

    // Get all items
    @GetMapping
    public ResponseEntity<List<ItemResponseDTO>> getAllItems() {
        List<ItemResponseDTO> items = itemService.getAllItems();
        return ResponseEntity.ok(items);
    }

    // Get item by ID
    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> getItemById(@PathVariable Long id) {
        ItemResponseDTO item = itemService.getItemById(id);
        return ResponseEntity.ok(item);
    }

    //Update item
    @PutMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> updateItem(@PathVariable Long id, @RequestBody ItemRequestDTO dto) {
        ItemResponseDTO updatedItem = itemService.updateItem(id, dto);
        return ResponseEntity.ok(updatedItem);
    }

//    // Delete item
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteItem(@PathVariable Long id) {
//        String message = itemService.deleteItem(id);
//        return ResponseEntity.ok(message);
//    }

}
