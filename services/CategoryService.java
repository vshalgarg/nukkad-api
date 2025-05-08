package com.neepanlokInfotech.nukkad_App.services;

import com.neepanlokInfotech.nukkad_App.dto.CreateCategoryDTO;
import com.neepanlokInfotech.nukkad_App.dto.GetCategoryDTO;
import com.neepanlokInfotech.nukkad_App.entities.Category;
import com.neepanlokInfotech.nukkad_App.mapper.CategoryMapper;
import com.neepanlokInfotech.nukkad_App.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CategoryService {

@Autowired
 private CategoryRepository categoryRepository;

 // to save category
   public Category saveCategory(CreateCategoryDTO categoryDto){
        Category category = new Category();
        category.setCategoryName(categoryDto.getCategoryName());
       return categoryRepository.save(category);
   }

// to get category by id
public GetCategoryDTO getCategoryDtoById(Long id) {
    Category category = categoryRepository.findById(id)
            .orElse(null);
    if (category == null)
        return null;

    return CategoryMapper.toDTO(category);
}

    // Get all categories
    public List<GetCategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<GetCategoryDTO> dtoList = new ArrayList<>();

        for (Category category : categories) {
            dtoList.add(CategoryMapper.toDTO(category));
        }
        return dtoList;
    }

    // update Category
    public GetCategoryDTO updateCategory(Long id, CreateCategoryDTO dto) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        existingCategory.setCategoryName(dto.getCategoryName());

        Category updatedCategory = categoryRepository.save(existingCategory);

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
