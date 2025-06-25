package com.code.monks.nukkad.services;

import com.code.monks.nukkad.context.UserContextHolder;
import com.code.monks.nukkad.dto.request.CreateStorekeeperRequestDTO;
import com.code.monks.nukkad.dto.response.CreateStorekeeperResponseDTO;
import com.code.monks.nukkad.entities.ImageEntity;
import com.code.monks.nukkad.entities.StorekeeperEntity;
import com.code.monks.nukkad.enums.RoleEnum;
import com.code.monks.nukkad.exception.AccessDeniedException;
import com.code.monks.nukkad.exception.ResourceNotFoundException;
import com.code.monks.nukkad.repositories.StorekeeperRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static com.code.monks.nukkad.enums.ResponseErrorCodes.ACCESS_DENIED_FOR_CUSTOMER_EXCEPTION;
import static com.code.monks.nukkad.enums.ResponseErrorCodes.STOREKEEPER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorekeeperService {

    private final StorekeeperRepository storekeeperRepository;


    public CreateStorekeeperResponseDTO createStoreKeeper(CreateStorekeeperRequestDTO dto) {
        if (!UserContextHolder.getUser().getRoles().contains(RoleEnum.STOREKEEPER)) {
            log.warn("[CREATE STOREKEEPER] Access denied: User is not a STOREKEEPER");
            throw new AccessDeniedException(ACCESS_DENIED_FOR_CUSTOMER_EXCEPTION);
        }
        Long storekeeperId = UserContextHolder.getUser().getId();
        String mobileNumber = UserContextHolder.getUser().getMobileNumber();
        log.info("[CREATE STOREKEEPER] Creating new storekeeper profile for storekeeperId={} and mobileNumber={}", storekeeperId,mobileNumber);

        String storeId = generateUniqueStoreId(mobileNumber);
        StorekeeperEntity storekeeper = CreateStorekeeperRequestDTO.toEntity(dto);
        storekeeper.setId(storekeeperId);
        storekeeper.setStoreId(storeId);
        storekeeper.setMobileNumber(mobileNumber);
//
//        // Upload and map images
//        String basePath = System.getProperty("user.home") + "/Desktop/storekeeper-images/";
//        File dir = new File(basePath);
//        if (!dir.exists()) dir.mkdirs();
//
//        List<ImageEntity> imageEntities = new ArrayList<>();
//        for (MultipartFile file : images) {
//            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
//            Path path = Paths.get(basePath + fileName);
//            try {
//                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//                String imageUrl = "file://" + path.toAbsolutePath();
//
//                ImageEntity image = new ImageEntity();
//                image.setImageUrl(imageUrl);
//                image.setStorekeeper(storekeeper);
//                imageEntities.add(image);
//
//            } catch (IOException e) {
//                throw new RuntimeException("Image upload failed: " + file.getOriginalFilename());
//            }
//        }
//
//        storekeeper.setStoreImages(imageEntities);

        StorekeeperEntity saved = storekeeperRepository.save(storekeeper);
        log.info("[CREATE STOREKEEPER] Storekeeper created successfully with ID: {}", saved.getId());

        return CreateStorekeeperResponseDTO.fromEntity(saved);
    }

    public CreateStorekeeperResponseDTO updateStoreKeeper(CreateStorekeeperRequestDTO dto) {
        if (!UserContextHolder.getUser().getRoles().contains(RoleEnum.STOREKEEPER)) {
            log.warn("[UPDATE STOREKEEPER] Access denied: User is not a STOREKEEPER");
            throw new AccessDeniedException(ACCESS_DENIED_FOR_CUSTOMER_EXCEPTION);
        }
        Long storekeeperId = UserContextHolder.getUser().getId();
        log.info("[UPDATE STOREKEEPER] Updating storekeeper with ID: {}", storekeeperId);

        StorekeeperEntity storekeeper = storekeeperRepository.findById(storekeeperId)
                .orElseThrow(() -> {
                    log.error("[UPDATE STOREKEEPER] Storekeeper not found with ID: {}", storekeeperId);
                    return new ResourceNotFoundException(STOREKEEPER_NOT_FOUND,storekeeperId);
                });

        storekeeper.setName(dto.getName());
        storekeeper.setStoreName(dto.getStoreName());
        storekeeper.setGstIn(dto.getGstIn());
        storekeeper.setAddressLine1(dto.getAddressLine1());
        storekeeper.setAddressLine2(dto.getAddressLine2());
        storekeeper.setCity(dto.getCity());
        storekeeper.setState(dto.getState());
        storekeeper.setPincode(dto.getPincode());

        StorekeeperEntity updated = storekeeperRepository.save(storekeeper);
        log.info("[UPDATE STOREKEEPER] Storekeeper updated successfully with ID: {}", updated.getId());

        return CreateStorekeeperResponseDTO.fromEntity(updated);
    }

    private String generateUniqueStoreId(String mobileNumber) {
        int suffix = 1;
        String storeId;

        do {
            storeId = "STR" + mobileNumber + suffix;
            suffix++;
        } while (storekeeperRepository.existsByStoreId(storeId));

        return storeId;
    }
}
