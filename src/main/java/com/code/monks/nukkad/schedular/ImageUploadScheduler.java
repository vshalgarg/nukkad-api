package com.code.monks.nukkad.schedular;

import com.code.monks.nukkad.entities.CategoryItemImageEntity;
import com.code.monks.nukkad.enums.ImageUploadStatusEnum;
import com.code.monks.nukkad.repositories.CategoryItemImageRepository;
import com.code.monks.nukkad.services.ImageSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageUploadScheduler {

    private final CategoryItemImageRepository imageRepository;
    private final ImageSyncService imageSyncService;

    @Scheduled(fixedDelay = 5000, initialDelay = 15000)
    public void uploadPendingImages() {

        long pendingCount = imageRepository
                .countByUploadStatus(ImageUploadStatusEnum.PENDING);

        if (pendingCount == 0) {
            return;
        }

        // work exists — log and start processing
        log.info("[SCHEDULER] {} PENDING images found — "
                + "starting sync", pendingCount);
        logCurrentProgress();

        List<CategoryItemImageEntity> pendingImages = imageRepository
                .findTop500ByUploadStatusOrderByIdAsc(
                        ImageUploadStatusEnum.PENDING);

        // safety check — in case count and fetch race condition
        if (pendingImages.isEmpty()) {
            return;
        }

        log.info("[SCHEDULER] Found {} PENDING images — "
                + "starting batch processing", pendingImages.size());

        imageSyncService.processBatch(pendingImages);

        // log updated progress after batch completes
        log.info("[SCHEDULER] Batch complete — updated progress:");
        logCurrentProgress();
    }

    private void logCurrentProgress() {

        long pendingCount = imageRepository
                .countByUploadStatus(ImageUploadStatusEnum.PENDING);
        long processingCount = imageRepository
                .countByUploadStatus(ImageUploadStatusEnum.PROCESSING);
        long uploadedCount = imageRepository
                .countByUploadStatus(ImageUploadStatusEnum.UPLOADED);
        long failedCount = imageRepository
                .countByUploadStatus(ImageUploadStatusEnum.FAILED);

        log.info("[SCHEDULER] Image sync progress — "
                        + "PENDING: {}, PROCESSING: {}, "
                        + "UPLOADED: {}, FAILED: {}",
                pendingCount, processingCount,
                uploadedCount, failedCount);

        if (processingCount > 0) {
            log.warn("[SCHEDULER] {} images stuck in PROCESSING — "
                            + "will be reset to PENDING on next startup",
                    processingCount);
        }

        if (failedCount > 0) {
            log.warn("[SCHEDULER] {} images permanently FAILED — "
                            + "check logs for upload errors",
                    failedCount);
        }
    }
}