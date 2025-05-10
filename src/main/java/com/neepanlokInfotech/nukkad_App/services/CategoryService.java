package com.neepanlokInfotech.nukkad_App.services;

import com.neepanlokInfotech.nukkad_App.dto.CreateCategoryDTO;
import com.neepanlokInfotech.nukkad_App.dto.GetCategoryDTO;
import com.neepanlokInfotech.nukkad_App.entities.CategoryEntity;
import com.neepanlokInfotech.nukkad_App.mapper.CategoryMapper;
import com.neepanlokInfotech.nukkad_App.repositories.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class CategoryService {

@Autowired
 private CategoryRepository categoryRepository;

    /*
        * this method will save category
     */
   public CategoryEntity saveCategory(CreateCategoryDTO categoryDto){
        CategoryEntity category = new CategoryEntity();
        category.setCategoryName(categoryDto.getCategoryName());
       CategoryEntity savedEntity = categoryRepository.save(category);
       log.info("category successfully saved to db: {}", savedEntity);
       return savedEntity;
   }

// to get category by id
public GetCategoryDTO getCategoryDtoById(Long id) {
    CategoryEntity category = categoryRepository.findById(id)
            .orElse(null);
//throw exception if not found
    if (category == null)
        return null;

    return CategoryMapper.toDTO(category);
}

    // Get all categories
    public List<GetCategoryDTO> getAllCategories() {
        List<CategoryEntity> categories = categoryRepository.findAll();
        List<GetCategoryDTO> dtoList = new ArrayList<>();

        for (CategoryEntity category : categories) {
            dtoList.add(CategoryMapper.toDTO(category));
        }
        return dtoList;
    }

    // update Category
    public GetCategoryDTO updateCategory(Long id, CreateCategoryDTO dto) {
        CategoryEntity existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        existingCategory.setCategoryName(dto.getCategoryName());

        CategoryEntity updatedCategory = categoryRepository.save(existingCategory);

        GetCategoryDTO updatedCategoryDTO = new GetCategoryDTO();
        updatedCategoryDTO.setCategoryId(updatedCategory.getCategoryId());
        updatedCategoryDTO.setCategoryName(updatedCategory.getCategoryName());

        return updatedCategoryDTO;
    }


    //delete category
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
