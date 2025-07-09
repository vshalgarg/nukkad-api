package com.code.monks.nukkad.services;

import com.code.monks.nukkad.context.UserContextHolder;
import com.code.monks.nukkad.dto.request.CreateStorekeeperRequestDTO;
import com.code.monks.nukkad.dto.response.CreateStorekeeperResponseDTO;
import com.code.monks.nukkad.dto.response.GetStorekeeperProfileResponseDTO;
import com.code.monks.nukkad.entities.StorekeeperEntity;
import com.code.monks.nukkad.entities.StorekeeperImageEntity;
import com.code.monks.nukkad.enums.RoleEnum;
import com.code.monks.nukkad.exception.AccessDeniedException;
import com.code.monks.nukkad.exception.DuplicateResourceException;
import com.code.monks.nukkad.exception.ResourceNotFoundException;
import com.code.monks.nukkad.utils.FileUploadHelper;
import com.code.monks.nukkad.repositories.StorekeeperImageRepository;
import com.code.monks.nukkad.repositories.StorekeeperRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.code.monks.nukkad.enums.ResponseErrorCodes.*;
import static com.code.monks.nukkad.utils.ExceptionUtil.extractFieldConflictMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorekeeperService {

    private final StorekeeperRepository storekeeperRepository;
    private final StorekeeperImageRepository storekeeperImageRepository;
    private final FileUploadHelper fileUploadHelper;


    public CreateStorekeeperResponseDTO createStoreKeeper(CreateStorekeeperRequestDTO dto, MultipartFile[] images) {
        if (!UserContextHolder.getUser().getRoles().contains(RoleEnum.STOREKEEPER)) {
            log.warn("[CREATE STOREKEEPER] Access denied: User is not a STOREKEEPER");
            throw new AccessDeniedException(ACCESS_DENIED_FOR_CUSTOMER_EXCEPTION);
        }

        Long storekeeperId = UserContextHolder.getUser().getId();
        String mobileNumber = UserContextHolder.getUser().getMobileNumber();
        log.info("[CREATE STOREKEEPER] Creating storekeeper for ID={} and mobile={}", storekeeperId, mobileNumber);

        String storeQrId = generateUniqueStoreQrId(mobileNumber);
        StorekeeperEntity storekeeper = CreateStorekeeperRequestDTO.toEntity(dto);
        storekeeper.setId(storekeeperId);
        storekeeper.setStoreQrId(storeQrId);
        storekeeper.setMobileNumber(mobileNumber);

        StorekeeperEntity saved;
        try {
            saved = storekeeperRepository.save(storekeeper);
            log.info("[CREATE STOREKEEPER] Storekeeper saved with ID={}", saved.getId());
        } catch (DataIntegrityViolationException e) {
            log.error("[CREATE STOREKEEPER] Duplicate entry for GST_IN or Mobile. gstIn={}, mobile={}", dto.getGstIn(), mobileNumber, e);
            String rootCauseMessage = e.getRootCause() != null ? e.getRootCause().getMessage() : "";
            String cleanMessage = extractFieldConflictMessage(rootCauseMessage);
            throw new DuplicateResourceException(DUPLICATE_RESOURCE_EXCEPTION,cleanMessage);
        } catch (TransactionSystemException e) {
            log.error("[CREATE STOREKEEPER] Validation or transaction error", e);
            throw new RuntimeException("Invalid storekeeper data. Please check input.");
        } catch (Exception e) {
            log.error("[CREATE STOREKEEPER] Unexpected error while saving storekeeper", e);
            throw new RuntimeException("Something went wrong while creating the storekeeper.");
        }

        List<String> imageUrls = new ArrayList<>();

        if (images != null && images.length > 0) {
            for (MultipartFile image : images) {
                try {
                    String imageUrl = fileUploadHelper.storeFile(image);
                    StorekeeperImageEntity imageEntity = StorekeeperImageEntity.builder()
                            .storekeeper(saved)
                            .imageUrl(imageUrl)
                            .build();
                    storekeeperImageRepository.save(imageEntity);
                    imageUrls.add(imageUrl);
                } catch (Exception e) {
                    log.error("[CREATE STOREKEEPER] Failed to store image: {}", image.getOriginalFilename(), e);
                    // Optionally, continue storing others or throw an exception
                    throw new RuntimeException("Failed to upload store image: " + image.getOriginalFilename());
                }
            }
        }

        log.info("[CREATE STOREKEEPER] Storekeeper created successfully with {} image(s)", imageUrls.size());
        return CreateStorekeeperResponseDTO.fromEntity(saved, imageUrls);
    }

    public CreateStorekeeperResponseDTO updateStoreKeeper(CreateStorekeeperRequestDTO dto, MultipartFile[] newImages) {
        if (!UserContextHolder.getUser().getRoles().contains(RoleEnum.STOREKEEPER)) {
            log.warn("[UPDATE STOREKEEPER] Access denied: User is not a STOREKEEPER");
            throw new AccessDeniedException(ACCESS_DENIED_FOR_CUSTOMER_EXCEPTION);
        }

        Long storekeeperId = UserContextHolder.getUser().getId();
        log.info("[UPDATE STOREKEEPER] Updating storekeeper with ID: {}", storekeeperId);

        StorekeeperEntity storekeeper = storekeeperRepository.findById(storekeeperId)
                .orElseThrow(() -> {
                    log.error("[UPDATE STOREKEEPER] Storekeeper not found with ID: {}", storekeeperId);
                    return new ResourceNotFoundException(STOREKEEPER_NOT_FOUND, storekeeperId);
                });

        storekeeper.setName(dto.getName());
        storekeeper.setStoreName(dto.getStoreName());
        storekeeper.setGstIn(dto.getGstIn());
        storekeeper.setAddressLine1(dto.getAddressLine1());
        storekeeper.setAddressLine2(dto.getAddressLine2());
        storekeeper.setCity(dto.getCity());
        storekeeper.setState(dto.getState());
        storekeeper.setPincode(dto.getPincode());

        List<String> imageUrls = new ArrayList<>();

        if (newImages != null && newImages.length > 0) {
            // Delete existing images only if new ones are uploaded
            List<StorekeeperImageEntity> existingImages = storekeeperImageRepository.findByStorekeeperId(storekeeperId);
            if (!existingImages.isEmpty()) {
                storekeeperImageRepository.deleteAll(existingImages);
                log.info("[UPDATE STOREKEEPER] Deleted existing images for storekeeperId={}", storekeeperId);
            }

            // Save new images
            for (MultipartFile image : newImages) {
                String imageUrl = fileUploadHelper.storeFile(image);
                StorekeeperImageEntity imageEntity = StorekeeperImageEntity.builder()
                        .storekeeper(storekeeper)
                        .imageUrl(imageUrl)
                        .build();
                storekeeperImageRepository.save(imageEntity);
                imageUrls.add(imageUrl);
            }

            log.info("[UPDATE STOREKEEPER] Stored new images for storekeeperId={}", storekeeperId);
        } else {
            // No new images uploaded, retain old image URLs
            imageUrls = storekeeper.getImages().stream()
                    .map(StorekeeperImageEntity::getImageUrl)
                    .toList();
        }

        StorekeeperEntity updated = storekeeperRepository.save(storekeeper);
        log.info("[UPDATE STOREKEEPER] Storekeeper updated successfully with ID: {}", updated.getId());

        return CreateStorekeeperResponseDTO.fromEntity(updated, imageUrls);
    }

    public GetStorekeeperProfileResponseDTO getStorekeeperProfile() {
        if (!UserContextHolder.getUser().getRoles().contains(RoleEnum.STOREKEEPER)) {
            log.warn("[GET PROFILE] Access denied: User is not a STOREKEEPER");
            throw new AccessDeniedException(ACCESS_DENIED_FOR_CUSTOMER_EXCEPTION);
        }

        Long storekeeperId = UserContextHolder.getUser().getId();
        log.info("[GET PROFILE] Fetching profile for storekeeperId={}", storekeeperId);

        StorekeeperEntity storekeeper = storekeeperRepository.findById(storekeeperId)
                .orElseThrow(() -> {
                    log.error("[GET PROFILE] Storekeeper not found. ID={}", storekeeperId);
                    return new ResourceNotFoundException(STOREKEEPER_NOT_FOUND, storekeeperId);
                });

        return GetStorekeeperProfileResponseDTO.fromEntity(storekeeper);
    }


    private String generateUniqueStoreQrId(String mobileNumber) {
        int suffix = 1;
        String storeQrId;

        do {
            storeQrId = "STR" + mobileNumber + suffix;
            suffix++;
        } while (storekeeperRepository.existsByStoreQrId(storeQrId));

        return storeQrId;
    }
}
