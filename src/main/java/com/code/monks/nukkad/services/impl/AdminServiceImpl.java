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
import java.util.stream.IntStream;
import com.code.monks.nukkad.enums.ImageTypeEnum;
import com.code.monks.nukkad.enums.ImageUploadStatusEnum;
import com.code.monks.nukkad.enums.ResponseErrorCodes;
import com.code.monks.nukkad.enums.UnitEnum;
import com.code.monks.nukkad.exception.DuplicateResourceException;
import com.code.monks.nukkad.exception.InvalidRequestException;
import com.code.monks.nukkad.exception.UnhandledException;
import com.code.monks.nukkad.mapper.AdminMapper;
import com.code.monks.nukkad.repositories.*;
import com.code.monks.nukkad.services.AdminService;
import com.code.monks.nukkad.services.BatchPersistenceService;
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
    private final BatchPersistenceService batchPersistenceService;
    private final CategoryItemImageRepository categoryItemImageRepository;
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

    @Override
    public ImportJsonDataResponse importProductsToExistingCategories(
            Categories categoriesRequest) {
        log.debug("[ADMIN SERVICE][IMPORT] Received request payload: {}", categoriesRequest);

        // validation call
        validateJsonStructure(categoriesRequest);

        log.info("[ADMIN SERVICE] Starting bulk JSON import. Categories: {}",
                categoriesRequest.getCategories().size());

        Set<String> categoryNames = categoriesRequest
                .getCategories()
                .stream()
                .map(Category::getCategoryName)
                .collect(Collectors.toSet());
        log.debug("[ADMIN SERVICE][IMPORT] Extracted category names: {}", categoryNames);


        Set<String> allProductNames = categoriesRequest
                .getCategories()
                .stream()
                .flatMap(category -> category.getProducts().stream())
                .map(Products::getName)
                .collect(Collectors.toSet());
        log.debug("[ADMIN SERVICE][IMPORT] Extracted product names count: {}", allProductNames.size());

        Map<String, CategoryEntity> categoryMap = categoryRepository
                .findAllByNameIn(categoryNames)
                .stream()
                .collect(Collectors.toMap(
                        CategoryEntity::getName,
                        Function.identity()
                ));
        log.debug("[ADMIN SERVICE][DB] Categories fetched from DB: {}", categoryMap.keySet());

        Map<String, Set<String>> existingProductCategoryMap = new HashMap<>();
        if (!allProductNames.isEmpty()) {
            List<Object[]> existingProducts = itemRepository
                    .findExistingProductsWithCategory(allProductNames);

            for (Object[] row : existingProducts) {
                existingProductCategoryMap
                        .computeIfAbsent((String) row[0], productName -> new HashSet<>())
                        .add((String) row[1]);

            }
        }

        List<ItemEntity> productBatch = new ArrayList<>();

        int totalProductsProcessed=0;
        int totalImagesQueued = 0;
        final int BATCH_SIZE = 500;

        for (Category categoryData : categoriesRequest.getCategories()) {
            String categoryName = categoryData.getCategoryName();
            log.debug("[ADMIN SERVICE][CATEGORY] Processing category: {}", categoryName);

            CategoryEntity category = getOrCreateCategory(categoryMap, categoryName,categoryData.getImageUrl());//category creation method called

            for (Products productData : categoryData.getProducts()) {
                String productName = productData.getName();
                log.debug("[ADMIN SERVICE][PRODUCT] Processing product: {}", productName);
                Set<String> dbCategories = existingProductCategoryMap
                        .getOrDefault(productName, new HashSet<>());
                if (dbCategories.contains(categoryName)) {
                    log.info("[ADMIN SERVICE] Skipping '{}' — already exists "
                            + "in category '{}'.", productName, categoryName);
                    continue;
                }

                if (!dbCategories.isEmpty()) {
                    log.info("[ADMIN SERVICE] Product '{}' exists in other "
                                    + "categories {} — linking to new category '{}'.",
                             productName, dbCategories, categoryName);
                             ItemEntity existingItem = itemRepository
                                     .findByNameWithCategoriesAndImages(productName)
                            .orElseThrow(() -> new UnhandledException(ResponseErrorCodes.UNHANDLED_EXCEPTION,
                                    new RuntimeException("Critical: Product " + productName + " not found in DB")
                            ));

                    if (!existingItem.getCategories().contains(category)) {
                        existingItem.getCategories().add(category);
                    }

                      Set<String> existingImageUrls = categoryItemImageRepository
                              .findImageUrlsByItemId(existingItem.getId());

                    int newImagesAdded = 0;
                    for (String imageUrl : productData.getImageUrls()) {
                        if (!existingImageUrls.contains(imageUrl)) {
                            CategoryItemImageEntity newImage = new CategoryItemImageEntity();
                            newImage.setImageUrl(imageUrl);
                            newImage.setItem(existingItem);
                            newImage.setImageType(ImageTypeEnum.PRODUCT);
                            newImage.setUploadStatus(ImageUploadStatusEnum.PENDING);
                            newImage.setRetryCount(0);
                            existingItem.addImage(newImage);
                            newImagesAdded++;
                            log.info("[ADMIN SERVICE] New image queued for "
                                            + "existing product '{}' → '{}'",
                                              productName, imageUrl);
                        } else {
                            log.debug("[ADMIN SERVICE] Image already exists "
                                            + "for product '{}' → '{}' skipped.",
                                               productName, imageUrl);
                        }
                    }
                    totalImagesQueued += newImagesAdded;
                    if (!productBatch.contains(existingItem)) {
                        productBatch.add(existingItem);}
                } else {
                    ItemEntity newItem = createProductEntity(productData, category);
                    createProductImages(newItem, productData.getImageUrls());
                    totalImagesQueued += productData.getImageUrls().size();
                    productBatch.add(newItem);
                }

                if (productBatch.size() >= BATCH_SIZE) {
                    batchPersistenceService.saveBatch(productBatch);
                    totalProductsProcessed += productBatch.size();
                    productBatch.clear();
                    categoryMap.replaceAll((name, cat)
                            -> batchPersistenceService.reattachCategory(cat));
                }
            }
        }
        if (!productBatch.isEmpty()) {
            batchPersistenceService.saveBatch(productBatch);
            totalProductsProcessed += productBatch.size();
        }
        log.info("[ADMIN SERVICE] Import complete. Products processed: {}...",
                totalProductsProcessed, totalImagesQueued);

        return new ImportJsonDataResponse(
                "Successfully processed " + totalProductsProcessed + " products. "
                        + totalImagesQueued + " images queued for background upload."
        );
    }
    private CategoryEntity getOrCreateCategory(Map<String, CategoryEntity> categoryMap, String categoryName, String imageUrl ) {
        // ADDED — imageUrl parameter
        log.debug("[ADMIN SERVICE][CATEGORY] Checking category existence: {}", categoryName);
        CategoryEntity category = categoryMap.get(categoryName);
        if (category == null) {
            category = new CategoryEntity();
            category.setName(categoryName);

            if (imageUrl != null && !imageUrl.isBlank()) {
                CategoryItemImageEntity image = new CategoryItemImageEntity();
                image.setImageUrl(imageUrl);
                image.setItem(null);
                image.setImageType(ImageTypeEnum.CATEGORY);
                image.setUploadStatus(ImageUploadStatusEnum.PENDING);
                image.setRetryCount(0);
                category.setImage(image);
                log.info("[ADMIN SERVICE] Category '{}' image queued: {}",
                        categoryName, imageUrl);
            }

            category = categoryRepository.save(category);
            categoryMap.put(categoryName, category);
            log.info("Created new category: {}", categoryName);
        }
        return category;
    }
    private ItemEntity createProductEntity(Products productData, CategoryEntity category) {
        log.debug("[ADMIN SERVICE][PRODUCT] Initializing entity for product: {}", productData.getName());
        ItemEntity product = new ItemEntity();
        product.setName(productData.getName());
        product.setUnit(productData.getUnit());
        product.setCategories(new ArrayList<>());
        product.getCategories().add(category);
        product.setImages(new ArrayList<>());
        return product;
    }
        private void createProductImages(ItemEntity product, List<String> imageUrls) {
            log.debug("[ADMIN SERVICE][IMAGE] Adding {} images to new product", imageUrls.size());
            for (String imageUrl : imageUrls) {
                log.debug("[ADMIN SERVICE][IMAGE] Adding image URL: {}", imageUrl);
                CategoryItemImageEntity image = new CategoryItemImageEntity();
                image.setImageUrl(imageUrl);
                image.setItem(product);
                image.setImageType(ImageTypeEnum.PRODUCT);
                image.setUploadStatus(ImageUploadStatusEnum.PENDING); // For your Keyset Pagination worker
                image.setRetryCount(0);
                product.addImage(image);
            }
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
        List<CustomerEntity> customers = customerRepository.findAllByStorekeepers_Id(id);
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

    private void validateJsonStructure(Categories categoriesRequest) {

        log.info("[JSON VALIDATION] Starting duplicate and warning checks. "
                        + "Total categories in JSON: {}",
                categoriesRequest.getCategories().size());

        List<String> errors = new ArrayList<>();

        Set<String> seenCategoryNames = new HashSet<>();

        for (int categoryIndex = 0;
             categoryIndex < categoriesRequest.getCategories().size();
             categoryIndex++) {

            Category currentCategory = categoriesRequest
                    .getCategories().get(categoryIndex);

            String trimmedCategoryName = currentCategory.getCategoryName().trim();

            if (!seenCategoryNames.add(trimmedCategoryName)) {
                errors.add("Category[" + categoryIndex + "] '"
                        + trimmedCategoryName
                        + "' is a duplicate category name in this JSON. "
                        + "Each category name must appear only once per import.");
            }
            //change ADDED — CHECK 2: validate category imageUrl format if provided

            if (currentCategory.getImageUrl() != null
                    && !currentCategory.getImageUrl().isBlank()
                    && !currentCategory.getImageUrl().startsWith("http://")
                    && !currentCategory.getImageUrl().startsWith("https://")) {
                errors.add("Category[" + categoryIndex + "] '"
                        + trimmedCategoryName
                        + "' imageUrl '" + currentCategory.getImageUrl()
                        + "' must start with http:// or https://");
            }

            Set<String> seenProductNamesInCurrentCategory = new HashSet<>();
            for (int productIndex = 0;
                 productIndex < currentCategory.getProducts().size();
                 productIndex++) {
                Products currentProduct = currentCategory
                        .getProducts().get(productIndex);

                String trimmedProductName = currentProduct.getName().trim();

               if (!seenProductNamesInCurrentCategory.add(trimmedProductName)) {
//
                log.warn("[JSON VALIDATION] Duplicate product skipped — "
                                + "Category['{}'] → Product['{}'] at index [{}]. "
                                + "First occurrence will be used.",
                        trimmedCategoryName,
                        trimmedProductName,
                        productIndex);
            }
                // CHECK - bad product image URL — hard ERROR
                for (int imageIndex = 0;
                     imageIndex < currentProduct.getImageUrls().size();
                     imageIndex++) {
                    String currentImageUrl = currentProduct
                            .getImageUrls().get(imageIndex);
                    if (currentImageUrl != null
                            && !currentImageUrl.isBlank()
                            && !currentImageUrl.startsWith("http://")
                            && !currentImageUrl.startsWith("https://")
                            && !currentImageUrl.equals("images/default.jpg")) {
                        errors.add("Category['" + trimmedCategoryName
                                + "'] → Product['" + trimmedProductName
                                + "'] → imageUrl[" + imageIndex + "] '"
                                + currentImageUrl
                                + "' must start with http:// or https://");
                    }
                }
            }
        }
            throwIfErrors(errors);
            log.info("[JSON VALIDATION] PASSED — all checks passed. Proceeding to import.");
        }
        private void throwIfErrors (List < String > errors) {
            if (!errors.isEmpty()) {
                String fullErrorReport = "[JSON VALIDATION] FAILED — "
                        + errors.size() + " error(s) found:\n"
                        + IntStream.range(0, errors.size())
                        .mapToObj(errorIndex ->
                                "  [" + (errorIndex + 1) + "] " + errors.get(errorIndex))
                        .collect(Collectors.joining("\n"));
                log.error(fullErrorReport);
                throw new InvalidRequestException(UNHANDLED_EXCEPTION, fullErrorReport);
            }
        }
    }



