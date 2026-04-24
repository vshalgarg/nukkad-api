package com.code.monks.nukkad.services;

import com.code.monks.nukkad.configuration.FirebaseImageProcessThreadPoolConfig;
import com.code.monks.nukkad.constants.FirebaseConstants;
import com.code.monks.nukkad.entities.CategoryItemImageEntity;
import com.code.monks.nukkad.enums.ImageUploadStatusEnum;
import com.code.monks.nukkad.repositories.CategoryItemImageRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Slf4j
@Service
public class ImageSyncService {


    private final CategoryItemImageRepository imageRepository;
    private final FirebaseStorageService firebaseStorageService;

    @Qualifier(FirebaseImageProcessThreadPoolConfig.IMAGE_UPLOAD_EXECUTOR)
    private final ExecutorService imageUploadExecutor;

    public ImageSyncService(
            CategoryItemImageRepository imageRepository,
            FirebaseStorageService firebaseStorageService,
            @Qualifier(FirebaseImageProcessThreadPoolConfig.IMAGE_UPLOAD_EXECUTOR)
            ExecutorService imageUploadExecutor) {
        this.imageRepository = imageRepository;
        this.firebaseStorageService = firebaseStorageService;
        this.imageUploadExecutor = imageUploadExecutor;
    }
    private static final int MAX_RETRY_COUNT = 3;
    private static final int THREAD_COUNT = 20;

    @PostConstruct
    public void resetStuckProcessingImagesOnStartup() {
        log.info("[IMAGE SYNC] Resetting stuck PROCESSING images to PENDING on startup");
        imageRepository.resetStuckProcessingImages();
        log.info("[IMAGE SYNC] Startup reset complete");
    }
    public void processBatch(List<CategoryItemImageEntity> pendingImages) {

        if (pendingImages.isEmpty()) {
            log.debug("[IMAGE SYNC] No pending images — skipping batch");
            return;
        }
        log.info("[IMAGE SYNC] Processing batch of {} images", pendingImages.size());

        List<Long> imageIds = pendingImages.stream()
                .map(CategoryItemImageEntity::getId)
                .toList();
        imageRepository.updateStatusByIds(
                imageIds,
                ImageUploadStatusEnum.PROCESSING,
                LocalDateTime.now()
        );
        log.info("[IMAGE SYNC] Marked {} images as PROCESSING", imageIds.size());
        List<List<CategoryItemImageEntity>> groups = splitIntoGroups(
                pendingImages, THREAD_COUNT);
        log.info("[IMAGE SYNC] Split into {} groups of ~{} images each",
                groups.size(), pendingImages.size() / THREAD_COUNT);
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (List<CategoryItemImageEntity> group : groups) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(
                    () -> processGroup(group),  // each thread processes its group
                    imageUploadExecutor          // uses our 10-thread pool
            );
            futures.add(future);
        }
        CompletableFuture
                .allOf(futures.toArray(new CompletableFuture[0]))
                .join();
        log.info("[IMAGE SYNC] Batch of {} images completed", pendingImages.size());
    }

    private void processGroup(List<CategoryItemImageEntity> group) {

        for (CategoryItemImageEntity image : group) {
            try {
                uploadSingleImage(image);
            } catch (Exception e) {
                log.error("[IMAGE SYNC] Unexpected error processing image id: {}. "
                        + "Reason: {}", image.getId(), e.getMessage());
                handleFailure(image);
            }
        }
    }
    private void uploadSingleImage(CategoryItemImageEntity image) {

        if (image.getImageUrl().startsWith("https://firebasestorage.googleapis.com/")
                && image.getImageUrl().contains("token=")) {

            log.info("[IMAGE SYNC] Image id: {} already has valid Firebase URL — skipping upload",
                    image.getId());

            imageRepository.markAsUploaded(
                    image.getId(),
                    image.getImageUrl(),
                    LocalDateTime.now()
            );
            return;
        }


        if (!image.getImageUrl().startsWith("http")) {
                log.error("[IMAGE SYNC] Image id: {} has invalid URL: '{}' — "
                            + "not a valid HTTP URL. Marking FAILED immediately.",
                    image.getId(), image.getImageUrl());
            imageRepository.markAsFailed(
                    image.getId(),
                    LocalDateTime.now()
            );
            return;
        }
        log.info("[IMAGE SYNC] Thread: {} uploading image id: {}, url: {}",
                Thread.currentThread().getName(),
                image.getId(),
                image.getImageUrl());
        // call FirebaseStorageService — throws ExternalServiceException on failure
        String firebaseUrl = firebaseStorageService.uploadImageFromUrl(
                image.getImageUrl(),
                FirebaseConstants.PRODUCT_IMAGE_TYPE
        );
        // upload succeeded — update DB with Firebase URL
        imageRepository.markAsUploaded(
                image.getId(),
                firebaseUrl,
                LocalDateTime.now()
        );
        log.info("[IMAGE SYNC] Image id: {} uploaded successfully. Firebase URL: {}",
                image.getId(), firebaseUrl);
    }
    private void handleFailure(CategoryItemImageEntity image) {

        int currentRetryCount = image.getRetryCount();

        if (currentRetryCount < MAX_RETRY_COUNT) {
            imageRepository.incrementRetryAndResetToPending(
                    image.getId(),
                    LocalDateTime.now()
            );
            log.warn("[IMAGE SYNC] Image id: {} failed. "
                            + "Retry {}/{}. Reset to PENDING.",
                    image.getId(),
                    currentRetryCount + 1,
                    MAX_RETRY_COUNT);
        } else {
            // exhausted all retries — mark permanently FAILED
            imageRepository.markAsFailed(
                    image.getId(),
                    LocalDateTime.now()
            );
            log.error("[IMAGE SYNC] Image id: {} permanently FAILED "
                            + "after {} retries. Manual review required.",
                    image.getId(),
                    MAX_RETRY_COUNT);
        }
    }

    private <T> List<List<T>> splitIntoGroups(List<T> items, int groupCount) {
        List<List<T>> groups = new ArrayList<>();
        int totalItems = items.size();
        int groupSize = (int) Math.ceil((double) totalItems / groupCount);
        for (int i = 0; i < totalItems; i += groupSize) {
            int end = Math.min(i + groupSize, totalItems);
            groups.add(new ArrayList<>(items.subList(i, end)));
        }
        return groups;
    }
}
