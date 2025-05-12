package com.neepanlokInfotech.nukkad_App.controllers;


import com.neepanlokInfotech.nukkad_App.dto.ItemRequestDTO;
import com.neepanlokInfotech.nukkad_App.dto.ItemResponseDTO;
import com.neepanlokInfotech.nukkad_App.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.neepanlokInfotech.nukkad_App.constants.UrlConstants.*;

@RestController
@RequestMapping(ITEM)
public class ItemController {

    @Autowired
    private ItemService itemService;

    /*

    *Create new item

     */
    @PostMapping(SAVE_ITEM)
    public ResponseEntity<ItemResponseDTO> createItem(@RequestBody ItemRequestDTO dto) {
        ItemResponseDTO savedItem = itemService.createItem(dto);
        return ResponseEntity.ok(savedItem);
    }

    /*

    *Get all items

     */
    @GetMapping(GET_ALL_ITEMS)
    public ResponseEntity<List<ItemResponseDTO>> getAllItems() {
        List<ItemResponseDTO> items = itemService.getAllItems();
        return ResponseEntity.ok(items);
    }

    /*

     *Get item by ID

     */
    @GetMapping(GET_ITEM_BY_ID)
    public ResponseEntity<ItemResponseDTO> getItemById(@PathVariable Long id) {
        ItemResponseDTO item = itemService.getItemById(id);
        return ResponseEntity.ok(item);
    }

    /*

    *Update item

     */
    @PutMapping(UPDATE_ITEM_BY_ID)
    public ResponseEntity<ItemResponseDTO> updateItem(@PathVariable Long id, @RequestBody ItemRequestDTO dto) {
        ItemResponseDTO updatedItem = itemService.updateItem(id, dto);
        return ResponseEntity.ok(updatedItem);
    }


}
