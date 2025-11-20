package com.code.monks.nukkad.services.impl;

import com.code.monks.nukkad.admin.request.AdminLoginRequestDto;
import com.code.monks.nukkad.admin.request.AdminRegisterRequestDto;
import com.code.monks.nukkad.admin.response.AdminLoginResponseDto;
import com.code.monks.nukkad.admin.response.AdminRegisterResponseDto;
import com.code.monks.nukkad.auth.response.AuthAdminLoginResponseDTO;
import com.code.monks.nukkad.auth.response.AuthAdminRegisterResponseDTO;
import com.code.monks.nukkad.client.AuthRestClient;
import com.code.monks.nukkad.dto.request.ItemExcelDTO;
import com.code.monks.nukkad.entities.CategoryEntity;
import com.code.monks.nukkad.entities.CategoryItemImageEntity;
import com.code.monks.nukkad.entities.ItemEntity;
import com.code.monks.nukkad.enums.UnitEnum;
import com.code.monks.nukkad.exception.DuplicateResourceException;
import com.code.monks.nukkad.exception.UnhandledException;
import com.code.monks.nukkad.mapper.AdminMapper;
import com.code.monks.nukkad.repositories.CategoryRepository;
import com.code.monks.nukkad.repositories.ItemRepository;
import com.code.monks.nukkad.services.AdminService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.code.monks.nukkad.enums.ResponseErrorCodes.DUPLICATE_PRODUCT_FOUND;
import static com.code.monks.nukkad.enums.ResponseErrorCodes.UNHANDLED_EXCEPTION;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final AuthRestClient authRestClient;
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
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
    public void bulkCreateFromExcel(List<ItemExcelDTO> dtos) {
        log.info("Starting bulk create of items from Excel. Number of items: {}", dtos.size());
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
                log.debug("Saved item: {}", item.getName());
            } catch (DataIntegrityViolationException e) {
                log.warn("Duplicate product name detected: {}", dto.getName());
                throw new DuplicateResourceException(DUPLICATE_PRODUCT_FOUND);
            }
        }
        log.info("Completed bulk create of items.");
    }
}

