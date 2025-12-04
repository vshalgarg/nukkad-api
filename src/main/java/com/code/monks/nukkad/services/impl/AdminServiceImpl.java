package com.code.monks.nukkad.services.impl;

import com.code.monks.nukkad.admin.request.AdminLoginRequestDto;
import com.code.monks.nukkad.admin.request.AdminRegisterRequestDto;
import com.code.monks.nukkad.admin.response.AdminLoginResponseDto;
import com.code.monks.nukkad.admin.response.AdminRegisterResponseDto;
import com.code.monks.nukkad.auth.response.AuthAdminLoginResponseDTO;
import com.code.monks.nukkad.auth.response.AuthAdminRegisterResponseDTO;
import com.code.monks.nukkad.client.AuthRestClient;
import com.code.monks.nukkad.dto.jsonUpload.Categories;
import com.code.monks.nukkad.dto.jsonUpload.Category;
import com.code.monks.nukkad.dto.jsonUpload.ImportJsonDataResponse;
import com.code.monks.nukkad.dto.jsonUpload.Products;
import com.code.monks.nukkad.dto.request.ItemExcelDTO;
import com.code.monks.nukkad.dto.response.DeleteCustomerResponseDTO;
import com.code.monks.nukkad.dto.response.DeleteStorekeeperResponseDTO;
import com.code.monks.nukkad.dto.response.UploadExcelFileResponseDto;
import com.code.monks.nukkad.entities.CategoryEntity;
import com.code.monks.nukkad.entities.CategoryItemImageEntity;
import com.code.monks.nukkad.entities.CustomerEntity;
import com.code.monks.nukkad.entities.ItemEntity;
import com.code.monks.nukkad.enums.ResponseErrorCodes;
import com.code.monks.nukkad.enums.UnitEnum;
import com.code.monks.nukkad.exception.DuplicateResourceException;
import com.code.monks.nukkad.exception.ResourceNotFoundException;
import com.code.monks.nukkad.exception.UnhandledException;
import com.code.monks.nukkad.mapper.AdminMapper;
import com.code.monks.nukkad.repositories.CategoryRepository;
import com.code.monks.nukkad.repositories.CustomerRepository;
import com.code.monks.nukkad.repositories.ItemRepository;
import com.code.monks.nukkad.repositories.StorekeeperRepository;
import com.code.monks.nukkad.services.AdminService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.code.monks.nukkad.enums.ResponseErrorCodes.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final AuthRestClient authRestClient;
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final CustomerRepository customerRepository;
    private final StorekeeperRepository storekeeperRepository;
    @Override
    public AdminLoginResponseDto login(AdminLoginRequestDto loginRequestDto) {

        log.info("[ADMIN SERVICE] Login request received for email: {}", loginRequestDto.getEmail());
            // Call Auth Service
            AuthAdminLoginResponseDTO authLoginResponse = authRestClient.callAdminLoginApi(loginRequestDto);
            log.info("[ADMIN SERVICE] Successfully authenticated admin: {}", loginRequestDto.getEmail());

            AdminLoginResponseDto responseDto = AdminMapper.toAdminLoginResponseDto(authLoginResponse);
            log.debug("[ADMIN SERVICE] Login response prepared for admin: {}", loginRequestDto.getEmail());
            return responseDto;
    }

    @Override
    public AdminRegisterResponseDto register(AdminRegisterRequestDto registerRequestDto) {
        log.info("[ADMIN SERVICE] Register request received for email: {}", registerRequestDto.getEmail());

        AuthAdminRegisterResponseDTO authResponse = authRestClient.callAdminRegisterApi(registerRequestDto);

        String message = switch (authResponse.getStatus()) {
            case ACTIVE -> "Admin registered successfully and activated.";
            case INACTIVE -> "Admin registered successfully but inactive.";
        };

        log.info("[ADMIN SERVICE] Registration completed for username: {}, Status: {}",
                registerRequestDto.getEmail(), authResponse.getStatus());

        return new AdminRegisterResponseDto(message);
    }

    @Transactional
    public ImportJsonDataResponse importProductsToExistingCategories(Categories categoriesRequest) {
        log.info("Starting import of {} categories with products", categoriesRequest.getCategories().size());

        //validation - 1 query
        validateAllProductNamesUnique(categoriesRequest);

        // All category fetch
        Set<String> categoryNames = categoriesRequest.getCategories().stream()
                .map(Category::getCategoryName)
                .collect(Collectors.toSet());

        Map<String, CategoryEntity> categoryMap = categoryRepository.findAllByNameIn(categoryNames)
                .stream().collect(Collectors.toMap(CategoryEntity::getName, Function.identity()));

        int totalProductsProcessed = 0;
        int totalImagesProcessed = 0;
        List<String> processedCategories = new ArrayList<>();

        // Safe processing (validation already done)
        for (Category categoryData : categoriesRequest.getCategories()) {
            String categoryName = categoryData.getCategoryName();
            CategoryEntity category = validateCategoryExists(categoryMap, categoryName);

            int categoryProductsCount = 0;
            for (Products productData : categoryData.getProducts()) {
                ItemEntity product = createProductEntity(productData, category);
                List<CategoryItemImageEntity> images = createProductImages(product, productData.getImageUrls());

                itemRepository.save(product);
                totalProductsProcessed++;
                totalImagesProcessed += images.size();
                categoryProductsCount++;
            }

            processedCategories.add(categoryName + "(" + categoryProductsCount + ")");
        }

        String successMessage = String.format(
                "Successfully imported %d products with %d images across %d categories: %s",
                totalProductsProcessed, totalImagesProcessed, processedCategories.size(),
                processedCategories
        );

        log.info(successMessage);
        return new ImportJsonDataResponse(successMessage);
    }

    private void validateAllProductNamesUnique(Categories categoriesRequest) {
        Set<String> allProductNames = categoriesRequest.getCategories().stream()
                .flatMap(cat -> cat.getProducts().stream())
                .map(Products::getName)
                .collect(Collectors.toSet());

        if (allProductNames.isEmpty()) return;

        List<String> existingNames = itemRepository.findExistingNames(allProductNames);

        if (!existingNames.isEmpty()) {
            // First duplicate ka naam user-friendly message ke liye
            String duplicateName = existingNames.get(0);
            throw new DuplicateResourceException(
                    ResponseErrorCodes.DUPLICATE_PRODUCT_FOUND,
                    String.format("Duplicate product found: '%s'. Please remove duplicate or rename before import.", duplicateName)
            );
        }
    }

    private CategoryEntity validateCategoryExists(Map<String, CategoryEntity> categoryMap, String categoryName) {
        CategoryEntity category = categoryMap.get(categoryName);
        if (category == null) {
            throw new ResourceNotFoundException(
                    ResponseErrorCodes.CATEGORY_NOT_FOUND,
                    "Category not found: '" + categoryName + "'"
            );
        }
        return category;
    }

    @Override
    public DeleteCustomerResponseDTO deleteCustomerById(Long id) {

        boolean exists = customerRepository.existsById(id);
        if (!exists) {
            return DeleteCustomerResponseDTO.builder()
                    .customerId(id)
                    .message("Customer not found")
                    .success(false)
                    .build();
        }
        customerRepository.deleteById(id);

        return DeleteCustomerResponseDTO.builder()
                .customerId(id)
                .message("Customer deleted successfully")
                .success(true)
                .build();
    }

    @Transactional
    @Override
    public DeleteStorekeeperResponseDTO deleteStorekeeperById(Long id) {
        boolean exists = storekeeperRepository.existsById(id);
        if (!exists) {
            return DeleteStorekeeperResponseDTO.builder()
                    .storekeeperId(id)
                    .message("Storekeeper not found")
                    .success(false)
                    .build();
        }

        // Fetch all customers who have this storekeeper
        List<CustomerEntity> customers = customerRepository.findAllByStorekeepers_Id(id);

        // Remove the storekeeper from each customer's storekeepers list
        for (CustomerEntity customer : customers) {
            customer.getStorekeepers().removeIf(sk -> sk.getId().equals(id));
        }
        customerRepository.saveAll(customers);

        storekeeperRepository.deleteById(id);

        return DeleteStorekeeperResponseDTO.builder()
                .storekeeperId(id)
                .message("Storekeeper deleted successfully")
                .success(true)
                .build();
    }


    @Transactional
    public UploadExcelFileResponseDto createProductsFromExcel(List<ItemExcelDTO> dtos) {
        log.info("Starting bulk create of items from Excel. Number of items: {}", dtos.size());

        validateExcelProductNamesUnique(dtos);

        int processedCount = 0;

        for (ItemExcelDTO dto : dtos) {
            try {
                ItemEntity item = new ItemEntity();
                item.setName(dto.getName());
                item.setUnit(UnitEnum.valueOf(dto.getUnit()));

                List<CategoryEntity> categories = categoryRepository.findAllById(dto.getCategoryIds());
                item.setCategories(categories);

                List<CategoryItemImageEntity> images = new ArrayList<>();
                for (String url : dto.getImageUrls()) {
                    CategoryItemImageEntity image = new CategoryItemImageEntity();
                    image.setImageUrl(url);
                    image.setItem(item);
                    images.add(image);
                }
                item.setImages(images);

                itemRepository.save(item);
                processedCount++;
                log.debug("Saved item: {}", item.getName());
            } catch (Exception e) {
                log.error("Error while saving item: {}", dto.getName(), e);
                throw new UnhandledException(FAILED_UPLOAD_EXCEL_FILE, e);
            }
        }

        log.info("Successfully created {} items from Excel import", processedCount);
        return new UploadExcelFileResponseDto("Successfully uploaded excel file in db", dtos.size());
    }

    //Excel specific
    private void validateExcelProductNamesUnique(List<ItemExcelDTO> dtos) {
        if (dtos.isEmpty()) return;

        Set<String> allProductNames = dtos.stream()
                .map(ItemExcelDTO::getName)
                .collect(Collectors.toSet());

        List<String> existingNames = itemRepository.findExistingNames(allProductNames);

        if (!existingNames.isEmpty()) {
            String duplicateName = existingNames.get(0);
            log.warn("Duplicate product found before Excel insert: {}", duplicateName);
            throw new DuplicateResourceException(
                    ResponseErrorCodes.DUPLICATE_PRODUCT_FOUND,
                    String.format("Duplicate product found in Excel: '%s'. Please remove duplicate or rename before import.", duplicateName)
            );
        }
    }

    private ItemEntity createProductEntity(Products productData, CategoryEntity category) {
        ItemEntity product = new ItemEntity();
        product.setName(productData.getName());
        product.setUnit(productData.getUnit());
        product.setCategories(List.of(category));
        product.setImages(new ArrayList<>());
        return product;
    }

    private List<CategoryItemImageEntity> createProductImages(ItemEntity product, List<String> imageUrls) {
        List<CategoryItemImageEntity> images = new ArrayList<>();
        for (String imageUrl : imageUrls) {
            CategoryItemImageEntity image = new CategoryItemImageEntity();
            image.setImageUrl(imageUrl);
            image.setItem(product);
            product.getImages().add(image);
            images.add(image);
        }
        return images;
    }
}

