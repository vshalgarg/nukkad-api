package com.neepanlokInfotech.nukkad_App.services;

import com.neepanlokInfotech.nukkad_App.dto.CreateCategoryDTO;
import com.neepanlokInfotech.nukkad_App.dto.GetCategoryDTO;
import com.neepanlokInfotech.nukkad_App.entities.CategoryEntity;
import com.neepanlokInfotech.nukkad_App.exception.ResourceNotFoundException;
import com.neepanlokInfotech.nukkad_App.mapper.CategoryMapper;
import com.neepanlokInfotech.nukkad_App.repositories.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class CategoryService {

@Autowired
 private CategoryRepository categoryRepository;

    /*
        * this method will save category
     */
   public CreateCategoryDTO saveCategory(CreateCategoryDTO categoryDto){
       log.info("Attempting to save new category with name: {}", categoryDto.getCategoryName());

        CategoryEntity category = new CategoryEntity();
        category.setCategoryName(categoryDto.getCategoryName());
       CategoryEntity savedEntity = categoryRepository.save(category);

       log.info("category successfully saved to db: {}", savedEntity);

       CreateCategoryDTO createCategoryDTO = new CreateCategoryDTO();
       createCategoryDTO.setCategoryName(savedEntity.getCategoryName());
       return createCategoryDTO;
   }

/*

 *To get category by id

 */
public GetCategoryDTO getCategoryDtoById(Long id) {
    log.info("Fetching category with ID: {}", id);

    CategoryEntity category = categoryRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Category not found with ID: {}", id);
                return new ResourceNotFoundException("Category not found with id: " + id);
            });

    log.info("Category found: ID={}, Name={}", category.getCategoryId(), category.getCategoryName());
    return CategoryMapper.toDTO(category);
}


    /*

     *Get all categories

     */
    public List<GetCategoryDTO> getAllCategories() {
        log.info("Fetching all categories from the database.");

        List<CategoryEntity> categories = categoryRepository.findAll();

        if (categories.isEmpty()) {
            log.warn("No categories found in the database.");
        } else {
            log.info("Total categories found: {}", categories.size());
        }

        List<GetCategoryDTO> dtoList = categories.stream()
                .map(CategoryMapper::toDTO)
                .collect(Collectors.toList());

        return dtoList;
    }


    /*

     *update Category

     */
    public GetCategoryDTO updateCategory(Long id, CreateCategoryDTO dto) {
        log.info("Updating category with ID: {}", id);

        CategoryEntity existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Category not found with ID: {}", id);
                    return new ResourceNotFoundException("Category not found with id: " + id);
                });

        log.debug("Existing category before update: {}", existingCategory);

        existingCategory.setCategoryName(dto.getCategoryName());
        CategoryEntity updatedCategory = categoryRepository.save(existingCategory);

        log.info("Category successfully updated: {}", updatedCategory);

        GetCategoryDTO updatedCategoryDTO = new GetCategoryDTO();
        updatedCategoryDTO.setCategoryId(updatedCategory.getCategoryId());
        updatedCategoryDTO.setCategoryName(updatedCategory.getCategoryName());

        return updatedCategoryDTO;
    }



    /*

    *delete category

     */
//    public String deleteCategory(Long id) {
//        Category category = categoryRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
//
//        // Delete the category
//        categoryRepository.delete(category);
//
//        return "Category with ID " + id + " has been successfully deleted.";
//    }


}
