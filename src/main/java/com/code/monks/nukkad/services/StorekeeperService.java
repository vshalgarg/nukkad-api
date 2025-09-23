package com.code.monks.nukkad.services;

import com.code.monks.nukkad.context.UserContextHolder;
import com.code.monks.nukkad.dto.User;
import com.code.monks.nukkad.dto.request.StorekeeperRequestDTO;
import com.code.monks.nukkad.dto.response.StorekeeperResponseDTO;
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
    private final FirebaseFileUploadHelper firebaseFileUploadHelper;
    private final ExceptionHandleUtil exceptionHandleUtil;
    private final NotificationStatusService notificationStatusService;

    public StorekeeperResponseDTO createStoreKeeper(StorekeeperRequestDTO dto) {
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
        StorekeeperEntity storekeeper = StorekeeperRequestDTO.toEntity(dto);
        storekeeper.setId(storekeeperId);
        storekeeper.setStoreQrId(storeQrId);
        storekeeper.setMobileNumber(mobileNumber);
        // Validate for unique fields (email, gst, etc.)
        exceptionHandleUtil.validateStorekeeperUniqueFields(storekeeper);

        List<String> imgUrls = dto.getImageUrls();
        if (imgUrls == null) imgUrls = new ArrayList<>();
        imgUrls = imgUrls.stream()
                .filter(url -> url != null && !url.isBlank())
                .limit(4)
                .toList();
        try {
            StorekeeperEntity saved = storekeeperRepository.save(storekeeper);
            log.info("[CREATE STOREKEEPER] Storekeeper saved with ID={}", saved.getId());

            // Save image URLs as entities
            List<StorekeeperImageEntity> imageEntities = imgUrls.stream()
                    .map(url -> StorekeeperImageEntity.builder()
                            .storekeeper(saved)
                            .imageUrl(url)
                            .build())
                    .toList();
            storekeeper.getImages().clear();
            storekeeper.getImages().addAll(imageEntities);


            // Set default notification status ON
            notificationStatusService.initializeStatusIfAbsent();

            StorekeeperEntity updated = storekeeperRepository.save(storekeeper);
            log.info("[CREATE STOREKEEPER] Storekeeper created successfully with {} image(s)", imageEntities.size());
            return StorekeeperResponseDTO.fromEntity(updated, imgUrls);
        } catch (Exception e) {
            log.error("[CREATE STOREKEEPER] Unexpected error while saving storekeeper", e);
            throw new UnhandledException(UNHANDLED_EXCEPTION, e);
        }

    }


    public StorekeeperResponseDTO updateStoreKeeper(StorekeeperRequestDTO dto) {

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
            log.info("[UPDATE STOREKEEPER] Updating fields for storekeeper ID={}", storekeeperId);
            storekeeper.setName(dto.getName());
            storekeeper.setStoreName(dto.getStoreName());
            storekeeper.setContactNumber(dto.getContactNumber());
            storekeeper.setGstNum(dto.getGstNum());
            storekeeper.setAddressLine1(dto.getAddressLine1());
            storekeeper.setAddressLine2(dto.getAddressLine2());
            storekeeper.setLandmark(dto.getLandmark());
            storekeeper.setCity(dto.getCity());
            storekeeper.setState(dto.getState());
            storekeeper.setPincode(dto.getPincode());
            log.info("[UPDATE STOREKEEPER] Validating unique fields (GST, address, etc.) for storekeeper ID={}", storekeeperId);
            exceptionHandleUtil.validateStorekeeperUniqueFields(storekeeper);

            List<StorekeeperImageEntity> existingImages = storekeeperImageRepository.findByStorekeeperId(storekeeperId);
            if (!existingImages.isEmpty()) {
                log.info("[UPDATE STOREKEEPER] Deleting {} old image(s) for storekeeper ID={}", existingImages.size(), storekeeperId);
                storekeeperImageRepository.deleteAll(existingImages);
            } else {
                log.info("[UPDATE STOREKEEPER] No existing images to delete for storekeeper ID={}", storekeeperId);
            }

            List<String> imgUrls = dto.getImageUrls();
            if (imgUrls == null) imgUrls = new ArrayList<>();
            imgUrls = imgUrls.stream()
                    .filter(url -> url != null && !url.isBlank())
                    .limit(4)
                    .toList();

            List<StorekeeperImageEntity> newImageEntities = imgUrls.stream()
                    .map(url -> StorekeeperImageEntity.builder()
                            .storekeeper(storekeeper)
                            .imageUrl(url)
                            .build())
                    .toList();

            log.info("[UPDATE STOREKEEPER] Stored {} new image record(s) for storekeeper ID={}", newImageEntities.size(), storekeeperId);
            storekeeper.getImages().clear();
            storekeeper.getImages().addAll(newImageEntities);
            StorekeeperEntity updated = storekeeperRepository.save(storekeeper);
            log.info("[UPDATE STOREKEEPER] Successfully updated storekeeper ID={} with name='{}'", updated.getId(), updated.getName());
            return StorekeeperResponseDTO.fromEntity(updated, imgUrls);
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
