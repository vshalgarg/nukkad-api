package com.neepanlokInfotech.nukkad_App.controllers;

import com.neepanlokInfotech.nukkad_App.dto.CreateCategoryDTO;
import com.neepanlokInfotech.nukkad_App.dto.GetCategoryDTO;
import com.neepanlokInfotech.nukkad_App.entities.Category;
import com.neepanlokInfotech.nukkad_App.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

 // create Category
    @PostMapping
    public ResponseEntity<Category> saveCategory(@RequestBody CreateCategoryDTO category) {
       Category categoryInDB =  categoryService.saveCategory(category);
         return new ResponseEntity<>(categoryInDB, HttpStatus.CREATED);
    }

// get category by id
 @GetMapping("/{id}")
 public ResponseEntity<GetCategoryDTO> getCategoryById(@PathVariable Long id) {
    GetCategoryDTO categoryDTO = categoryService.getCategoryDtoById(id);
    return ResponseEntity.ok(categoryDTO);
 }

    // GET - Get all categories
     @GetMapping
    public ResponseEntity<List<GetCategoryDTO>> getAllCategories() {
        List<GetCategoryDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // Update category
    @PutMapping("/{id}")
    public ResponseEntity<GetCategoryDTO> updateCategory(@PathVariable Long id, @RequestBody CreateCategoryDTO dto) {

        GetCategoryDTO updatedCategory = categoryService.updateCategory(id, dto);

        return ResponseEntity.ok(updatedCategory);
    }

//    // Delete category
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
//        String message = categoryService.deleteCategory(id);
//        return new ResponseEntity<>(message , HttpStatus.OK);
//    }

}
