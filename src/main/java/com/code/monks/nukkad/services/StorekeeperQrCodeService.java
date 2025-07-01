package com.code.monks.nukkad.services;

import com.code.monks.nukkad.context.UserContextHolder;
import com.code.monks.nukkad.entities.StorekeeperEntity;
import com.code.monks.nukkad.entities.StorekeeperQrCodeEntity;
import com.code.monks.nukkad.repositories.StorekeeperQrCodeRepository;
import com.code.monks.nukkad.repositories.StorekeeperRepository;
import com.code.monks.nukkad.utils.FileUploadHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StorekeeperQrCodeService {
    private final StorekeeperQrCodeRepository qrRepo;
    private final StorekeeperRepository storekeeperRepo;
    private final FileUploadHelper fileUploadHelper;

    public void uploadQrCodes(MultipartFile[] qrCodes) {
        Long storekeeperId = UserContextHolder.getUser().getId();
        StorekeeperEntity storekeeper = storekeeperRepo.findById(storekeeperId).orElseThrow();

        int existing = qrRepo.countByStorekeeperId(storekeeperId);
        if (existing + qrCodes.length > 3)
            throw new RuntimeException("Only 3 QR codes allowed");

        boolean hasDefault = qrRepo.existsByStorekeeperIdAndIsDefaultTrue(storekeeperId);

        for (int i = 0; i < qrCodes.length; i++) {
            String url = fileUploadHelper.storeFile(qrCodes[i]);
            qrRepo.save(StorekeeperQrCodeEntity.builder()
                    .storekeeper(storekeeper)
                    .qrImageUrl(url)
                    .isDefault(!hasDefault && i == 0)
                    .build());
        }
    }

    public void deleteQr(Long qrId) {
        StorekeeperQrCodeEntity qr = qrRepo.findById(qrId).orElseThrow();
        if (qr.isDefault()) throw new RuntimeException("Cannot delete default QR");
        qrRepo.delete(qr);
    }

    public void markAsDefault(Long qrId) {
        StorekeeperQrCodeEntity selected = qrRepo.findById(qrId).orElseThrow();
        Long storekeeperId = selected.getStorekeeper().getId();

        List<StorekeeperQrCodeEntity> all = qrRepo.findByStorekeeperId(storekeeperId);
        for (StorekeeperQrCodeEntity qr : all) {
            qr.setDefault(qr.getId().equals(qrId));
        }
        qrRepo.saveAll(all);
    }

    public List<StorekeeperQrCodeEntity> getAllQrCodes() {
        Long storekeeperId = UserContextHolder.getUser().getId();
        return qrRepo.findByStorekeeperId(storekeeperId);
    }

    public StorekeeperQrCodeEntity updateQr(Long qrId, MultipartFile file) {
        StorekeeperQrCodeEntity qr = qrRepo.findById(qrId).orElseThrow();
        String url = fileUploadHelper.storeFile(file);
        qr.setQrImageUrl(url);
        return qrRepo.save(qr);
    }
}
