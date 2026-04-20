package com.code.monks.nukkad.repositories;

import com.code.monks.nukkad.entities.CategoryItemImageEntity;
import com.code.monks.nukkad.enums.ImageUploadStatusEnum;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface CategoryItemImageRepository extends JpaRepository<CategoryItemImageEntity, Long> {

    long countByUploadStatus(ImageUploadStatusEnum status);

    @Query("SELECT c.imageUrl FROM category_item_image c WHERE c.item.id = :itemId")
    Set<String> findImageUrlsByItemId(@Param("itemId") Long itemId);

    List<CategoryItemImageEntity> findTop500ByUploadStatusOrderByIdAsc(
            ImageUploadStatusEnum status);

    @Modifying
    @Transactional
    @Query("UPDATE category_item_image c "
            + "SET c.uploadStatus = :status, "
            + "c.lastSyncedAt = :now "
            + "WHERE c.id IN :ids")
    void updateStatusByIds(
            @Param("ids") List<Long> ids,
            @Param("status") ImageUploadStatusEnum status,
            @Param("now") LocalDateTime now
    );
    @Modifying
    @Transactional
    @Query("UPDATE category_item_image c "
            + "SET c.uploadStatus = 'UPLOADED', "
            + "c.imageUrl = :firebaseUrl, "
            + "c.lastSyncedAt = :now "
            + "WHERE c.id = :id")
    void markAsUploaded(
            @Param("id") Long id,
            @Param("firebaseUrl") String firebaseUrl,
            @Param("now") LocalDateTime now
    );

    @Modifying
    @Transactional
    @Query("UPDATE category_item_image c "
            + "SET c.uploadStatus = 'FAILED', "
            + "c.lastSyncedAt = :now "
            + "WHERE c.id = :id")
    void markAsFailed(
            @Param("id") Long id,
            @Param("now") LocalDateTime now
    );
    @Modifying
    @Transactional
    @Query("UPDATE category_item_image c "
            + "SET c.uploadStatus = 'PENDING', "
            + "c.retryCount = c.retryCount + 1, "
            + "c.lastSyncedAt = :now "
            + "WHERE c.id = :id")
    void incrementRetryAndResetToPending(
            @Param("id") Long id,
            @Param("now") LocalDateTime now
    );
    @Modifying
    @Transactional
    @Query("UPDATE category_item_image c "
            + "SET c.uploadStatus = 'PENDING' "
            + "WHERE c.uploadStatus = 'PROCESSING'")
    void resetStuckProcessingImages();

}