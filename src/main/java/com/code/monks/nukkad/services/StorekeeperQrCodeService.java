package com.code.monks.nukkad.services;

import com.code.monks.nukkad.context.UserContextHolder;
import com.code.monks.nukkad.dto.request.UpdateUploadQrCodeReqDTO;
import com.code.monks.nukkad.dto.request.UploadQrCodeReqDTO;
import com.code.monks.nukkad.dto.response.DeleteQrCodeResponseDto;
import com.code.monks.nukkad.dto.response.StorekeeperQrCodeResponseDTO;
import com.code.monks.nukkad.dto.response.UpdateStorekeeperQrResponseDTO;
import com.code.monks.nukkad.dto.response.UploadQrCodeResponseDto;
import com.code.monks.nukkad.entities.StorekeeperEntity;
import com.code.monks.nukkad.entities.StorekeeperQrCodeEntity;
import com.code.monks.nukkad.exception.DefaultQrCodeNotUpdatedException;
import com.code.monks.nukkad.exception.MaxQrLimitExceededException;
import com.code.monks.nukkad.exception.ResourceNotFoundException;
import com.code.monks.nukkad.repositories.StorekeeperQrCodeRepository;
import com.code.monks.nukkad.repositories.StorekeeperRepository;
import com.code.monks.nukkad.utils.FirebaseFileUploadHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.code.monks.nukkad.enums.ResponseErrorCodes.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorekeeperQrCodeService {
    private final StorekeeperQrCodeRepository qrRepo;
    private final StorekeeperRepository storekeeperRepo;
    private final FirebaseFileUploadHelper firebaseFileUploadHelper;

    public UploadQrCodeResponseDto uploadQrCodes(UploadQrCodeReqDTO uploadQrCodeReqDTO) {
        Long storekeeperId = UserContextHolder.getUser().getId();
        StorekeeperEntity storekeeper = storekeeperRepo.findById(storekeeperId)
                .orElseThrow(() -> new ResourceNotFoundException(STOREKEEPER_NOT_FOUND, storekeeperId));

        int existing = qrRepo.countByStorekeeperId(storekeeperId);

        if (existing + 1 > 3) {
            log.warn("[QR UPLOAD] Upload limit exceeded for storekeeperId={}", storekeeperId);
            throw new MaxQrLimitExceededException(QR_CODE_LIMIT);
        }

        boolean hasDefault = qrRepo.existsByStorekeeperIdAndIsDefaultTrue(storekeeperId);

        String url = uploadQrCodeReqDTO.getQrCodes();
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("QR Code URL must not be empty");
        }

        qrRepo.save(StorekeeperQrCodeEntity.builder()
                .storekeeper(storekeeper)
                .qrImageUrl(url)
                .isDefault(!hasDefault)
                .build());

        log.info("[QR UPLOAD] QR code saved with URL={} for storekeeperId={}", url, storekeeperId);
        log.info("[QR UPLOAD] Successfully uploaded 1 QR code for storekeeperId={}", storekeeperId);
        return new UploadQrCodeResponseDto("QR codes uploaded successfully");
    }


    public DeleteQrCodeResponseDto deleteQr(Long qrId) {
        StorekeeperQrCodeEntity qr = qrRepo.findById(qrId)
                .orElseThrow(() -> {
                    log.warn("[QR DELETE] QR code not found: ID={}", qrId);
                    return new ResourceNotFoundException(QR_CODE_NOT_FOUND);
                });

        if (qr.isDefault()) {
            log.warn("[QR DELETE] Attempted to delete default QR code: ID={}", qrId);
            throw new DefaultQrCodeNotUpdatedException(DEFAULT_QR_CODE_CAN_NOT_BE_DELETE);
        }

            // Delete file from Firebase storage
            firebaseFileUploadHelper.deleteFile(qr.getQrImageUrl());
            log.info("[QR DELETE] Deleted QR image from Firebase: URL={}", qr.getQrImageUrl());

        qrRepo.delete(qr);
        log.info("[QR DELETE] Successfully deleted QR code: ID={}", qrId);

        return new DeleteQrCodeResponseDto("QR code deleted successfully");
    }

    public void markAsDefault(Long qrId) {
        log.info("[QR MARK DEFAULT] Request received to mark QR code ID={} as default", qrId);

        StorekeeperQrCodeEntity selected = qrRepo.findById(qrId)
                .orElseThrow(() -> {
                    log.warn("[QR MARK DEFAULT] QR code not found for ID={}", qrId);
                    return new ResourceNotFoundException(QR_CODE_NOT_FOUND);
                });

        Long storekeeperId = selected.getStorekeeper().getId();
        log.info("[QR MARK DEFAULT] Storekeeper ID={} owns the QR code", storekeeperId);

        List<StorekeeperQrCodeEntity> all = qrRepo.findByStorekeeperId(storekeeperId);
        log.info("[QR MARK DEFAULT] Found {} QR codes for storekeeperId={}", all.size(), storekeeperId);

        for (StorekeeperQrCodeEntity qr : all) {
            boolean isDefault = qr.getId().equals(qrId);
            qr.setDefault(isDefault);
            if (isDefault) {
                log.info("[QR MARK DEFAULT] Marked QR code ID={} as default", qr.getId());
            }
        }

        qrRepo.saveAll(all);
        log.info("[QR MARK DEFAULT] Updated all QR codes for storekeeperId={}", storekeeperId);
    }


    public List<StorekeeperQrCodeResponseDTO> getAllQrCodes() {
        Long storekeeperId = UserContextHolder.getUser().getId();

        List<StorekeeperQrCodeEntity> qrCodes = qrRepo.findByStorekeeperId(storekeeperId);
        log.info("[QR FETCH] Found {} QR codes for storekeeperId={}", qrCodes.size(), storekeeperId);

        return qrCodes.stream()
                .map(StorekeeperQrCodeResponseDTO::fromEntity)
                .toList();
    }

    public UpdateStorekeeperQrResponseDTO updateQr(Long qrId, UpdateUploadQrCodeReqDTO qrCodeReqDTO){
        StorekeeperQrCodeEntity qr = qrRepo.findById(qrId)
                .orElseThrow(() -> new ResourceNotFoundException(QR_CODE_NOT_FOUND));

        // Delete old image if exists
        if (qr.getQrImageUrl() != null && !qr.getQrImageUrl().isEmpty()) {
            try {
                firebaseFileUploadHelper.deleteFile(qr.getQrImageUrl());
                log.info("[QR UPDATE] Deleted old QR image from Firebase: {}", qr.getQrImageUrl());
            } catch (Exception e) {
                log.warn("[QR UPDATE] Failed to delete old QR image from Firebase: {}", qr.getQrImageUrl(), e);
            }
        }
        String newImageUrl = qrCodeReqDTO.getQrImage();
        qr.setQrImageUrl(newImageUrl);
        StorekeeperQrCodeEntity updated = qrRepo.save(qr);

        log.info("[QR UPDATE] Updated QR code ID={} with new image URL={}", updated.getId(), updated.getQrImageUrl());

        return UpdateStorekeeperQrResponseDTO.fromEntity(updated);
    }


}
