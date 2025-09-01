package com.code.monks.nukkad.services;

import com.code.monks.nukkad.context.UserContextHolder;
import com.code.monks.nukkad.dto.User;
import com.code.monks.nukkad.dto.request.CreateStorekeeperRequestDTO;
import com.code.monks.nukkad.dto.response.CreateStorekeeperResponseDTO;
import com.code.monks.nukkad.dto.response.GetStorekeeperProfileResponseDTO;
import com.code.monks.nukkad.entities.StorekeeperEntity;
import com.code.monks.nukkad.entities.StorekeeperImageEntity;
import com.code.monks.nukkad.enums.RoleEnum;
import com.code.monks.nukkad.exception.AccessDeniedException;
import com.code.monks.nukkad.exception.DuplicateResourceException;
import com.code.monks.nukkad.exception.ResourceNotFoundException;
import com.code.monks.nukkad.exception.UnhandledException;
import com.code.monks.nukkad.repositories.StorekeeperImageRepository;
import com.code.monks.nukkad.repositories.StorekeeperRepository;
import com.code.monks.nukkad.utils.ExceptionHandleUtil;
import com.code.monks.nukkad.utils.FirebaseFileUploadHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.code.monks.nukkad.enums.ResponseErrorCodes.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class StorekeeperService {

    private final StorekeeperRepository storekeeperRepository;
    private final StorekeeperImageRepository storekeeperImageRepository;
    private final ExceptionHandleUtil exceptionHandleUtil;
    private final NotificationStatusService notificationStatusService;
    private final FirebaseFileUploadHelper firebaseFileUploadHelper;

    public CreateStorekeeperResponseDTO createStoreKeeper(CreateStorekeeperRequestDTO dto, MultipartFile[] images) {
        if (!UserContextHolder.getUser().getRoles().contains(RoleEnum.STOREKEEPER)) {
            log.warn("[CREATE STOREKEEPER] Access denied: User is not a STOREKEEPER");
            throw new AccessDeniedException(ACCESS_DENIED_FOR_CUSTOMER_EXCEPTION);
        }

        Long storekeeperId = UserContextHolder.getUser().getId();
        String mobileNumber = UserContextHolder.getUser().getMobileNumber();
        log.info("[CREATE STOREKEEPER] Creating storekeeper for ID={} and mobile={}", storekeeperId, mobileNumber);

        //  Check if storekeeper already exists
        if (storekeeperRepository.existsById(storekeeperId)) {
            log.warn("[CREATE STOREKEEPER] Storekeeper profile already exists for ID={}", storekeeperId);
            throw new DuplicateResourceException(DUPLICATE_STOREKEEPER_PROFILE_FOUND_EXCEPTION);
        }

        String storeQrId = generateUniqueStoreQrId(mobileNumber);
        StorekeeperEntity storekeeper = CreateStorekeeperRequestDTO.toEntity(dto);
        storekeeper.setId(storekeeperId);
        storekeeper.setStoreQrId(storeQrId);
        storekeeper.setMobileNumber(mobileNumber);

        // Validate for unique fields (email, gst, etc.)
        exceptionHandleUtil.validateStorekeeperUniqueFields(storekeeper);

        StorekeeperEntity saved;
        try {
            saved = storekeeperRepository.save(storekeeper);
            log.info("[CREATE STOREKEEPER] Storekeeper saved with ID={}", saved.getId());

            // Set default notification status ON
            notificationStatusService.initializeStatusIfAbsent();

        } catch (Exception e) {
            log.error("[CREATE STOREKEEPER] Unexpected error while saving storekeeper", e);
            throw new UnhandledException(UNHANDLED_EXCEPTION, e);
        }

        List<String> imageUrls = new ArrayList<>();

        if (images != null && images.length > 0) {
            for (MultipartFile image : images) {
                try {
                    // Upload to Firebase
                    String imageUrl = firebaseFileUploadHelper.uploadFile(image, "storekeepers");

                    StorekeeperImageEntity imageEntity = StorekeeperImageEntity.builder()
                            .storekeeper(saved)
                            .imageUrl(imageUrl)
                            .build();
                    storekeeperImageRepository.save(imageEntity);
                    imageUrls.add(imageUrl);
                    log.debug("[CREATE STOREKEEPER] Image uploaded to Firebase: {}", imageUrl);
                } catch (Exception e) {
                    log.error("[CREATE STOREKEEPER] Failed to upload image to Firebase: {}", image.getOriginalFilename(), e);
                    throw new UnhandledException(UNHANDLED_EXCEPTION, e);
                }
            }
        } else {
            log.info("[CREATE STOREKEEPER] No images provided.");
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

        try {
            // Update fields from DTO
            storekeeper.setName(dto.getName());
            storekeeper.setStoreName(dto.getStoreName());
            storekeeper.setContactNumber(dto.getContactNumber());
            storekeeper.setGstNum(dto.getGstNum());
            storekeeper.setAddressLine1(dto.getAddressLine1());
            storekeeper.setAddressLine2(dto.getAddressLine2());
            storekeeper.setCity(dto.getCity());
            storekeeper.setState(dto.getState());
            storekeeper.setPincode(dto.getPincode());

            // Validate unique fields (e.g. GST, address line 1)
            exceptionHandleUtil.validateStorekeeperUniqueFields(storekeeper);

            List<String> imageUrls = new ArrayList<>();

            // Handle new images
            if (newImages != null && newImages.length > 0) {
                try {
                    // Fetch existing images
                    List<StorekeeperImageEntity> existingImages = storekeeperImageRepository.findByStorekeeperId(storekeeperId);

                    if (!existingImages.isEmpty()) {
                        // Delete old images from Firebase
                        for (StorekeeperImageEntity oldImage : existingImages) {
                            if (oldImage.getImageUrl() != null && !oldImage.getImageUrl().isBlank()) {
                                firebaseFileUploadHelper.deleteFile(oldImage.getImageUrl());
                                log.info("[UPDATE STOREKEEPER] Deleted old image from Firebase: {}", oldImage.getImageUrl());
                            }
                        }

                        // Delete old images from DB
                        storekeeperImageRepository.deleteAll(existingImages);
                        log.info("[UPDATE STOREKEEPER] Deleted {} existing image record(s) for storekeeperId={}", existingImages.size(), storekeeperId);
                    }

                    // Upload new images
                    for (MultipartFile image : newImages) {
                        String imageUrl = firebaseFileUploadHelper.uploadFile(image, "storekeepers");
                        StorekeeperImageEntity imageEntity = StorekeeperImageEntity.builder()
                                .storekeeper(storekeeper)
                                .imageUrl(imageUrl)
                                .build();
                        storekeeperImageRepository.save(imageEntity);
                        imageUrls.add(imageUrl);
                    }

                    log.info("[UPDATE STOREKEEPER] Stored {} new image(s) for storekeeperId={}", imageUrls.size(), storekeeperId);
                } catch (Exception e) {
                    log.error("[UPDATE STOREKEEPER] Error while storing new images", e);
                    throw new UnhandledException(UNHANDLED_EXCEPTION, e);
                }
            } else {
                // Retain existing images if no new ones provided
                imageUrls = storekeeper.getImages().stream()
                        .map(StorekeeperImageEntity::getImageUrl)
                        .toList();
                log.info("[UPDATE STOREKEEPER] No new images uploaded. Retaining existing {} image(s).", imageUrls.size());
            }

            StorekeeperEntity updated = storekeeperRepository.save(storekeeper);
            log.info("[UPDATE STOREKEEPER] Successfully updated storekeeper ID={} with name='{}'", updated.getId(), updated.getName());

            return CreateStorekeeperResponseDTO.fromEntity(updated, imageUrls);

        } catch (DuplicateResourceException e) {
            log.warn("[UPDATE STOREKEEPER] Duplicate field(s) found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("[UPDATE STOREKEEPER] Unexpected error occurred while updating storekeeper", e);
            throw new UnhandledException(UNHANDLED_EXCEPTION, e);
        }
    }



    public GetStorekeeperProfileResponseDTO getStorekeeperProfile() {
        User user = UserContextHolder.getRequiredUser();

        if (!user.getRoles().contains(RoleEnum.STOREKEEPER)) {
            log.warn("[GET PROFILE] Access denied: User role={} is not STOREKEEPER", user.getRoles());
            throw new AccessDeniedException(ACCESS_DENIED_FOR_CUSTOMER_EXCEPTION);
        }

        Long storekeeperId = user.getId();
        log.info("[GET PROFILE] Initiating profile fetch for storekeeperId={}", storekeeperId);

        try {
            StorekeeperEntity storekeeper = storekeeperRepository.findById(storekeeperId)
                    .orElseThrow(() -> {
                        log.error("[GET PROFILE] Storekeeper not found with ID={}", storekeeperId);
                        return new ResourceNotFoundException(STOREKEEPER_NOT_FOUND, storekeeperId);
                    });

            log.info("[GET PROFILE] Storekeeper profile found. ID={}, Name={}", storekeeper.getId(), storekeeper.getName());
            return GetStorekeeperProfileResponseDTO.fromEntity(storekeeper);

        } catch (ResourceNotFoundException ex) {
            throw ex;
        } catch (Exception e) {
            log.error("[GET PROFILE] Unexpected error while fetching storekeeper profile. ID={}", storekeeperId, e);
            throw new UnhandledException(UNHANDLED_EXCEPTION, e);
        }
    }


    private String generateUniqueStoreQrId(String mobileNumber) {
        int suffix = 1;
        String storeQrId;

        do {
            storeQrId = "NKS" + mobileNumber + suffix;
            suffix++;
        } while (storekeeperRepository.existsByStoreQrId(storeQrId));

        return storeQrId;
    }
}
