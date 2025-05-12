package com.neepanlokInfotech.nukkad_App.controllers;

import com.neepanlokInfotech.nukkad_App.dto.CreateCategoryDTO;
import com.neepanlokInfotech.nukkad_App.dto.GetCategoryDTO;
import com.neepanlokInfotech.nukkad_App.entities.CategoryEntity;
import com.neepanlokInfotech.nukkad_App.services.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.neepanlokInfotech.nukkad_App.constants.UrlConstants.*;


@RestController
@RequestMapping(CATEGORY)
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /*

    *create Category

     */
    @PostMapping(CREATE_CATEGORY)
    public ResponseEntity<CreateCategoryDTO> saveCategory(@RequestBody CreateCategoryDTO category) {
        log.info("Received request for saveCategory: {}", category);
       CreateCategoryDTO categoryInDB =  categoryService.saveCategory(category);
         return new ResponseEntity<>(categoryInDB, HttpStatus.CREATED);
    }

/*

 *get category by id

 */
 @GetMapping(GET_CATEGORY_BY_ID)
 public ResponseEntity<GetCategoryDTO> getCategoryById(@PathVariable Long id) {
    GetCategoryDTO categoryDTO = categoryService.getCategoryDtoById(id);
    return ResponseEntity.ok(categoryDTO);
 }

   /*

   *Get all categories

    */
     @GetMapping(GET_ALL_CATEGORY)
    public ResponseEntity<List<GetCategoryDTO>> getAllCategories() {
        List<GetCategoryDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    /*

    *Update category

     */
    @PutMapping(UPDATE_CATEGORY_BY_ID)
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
