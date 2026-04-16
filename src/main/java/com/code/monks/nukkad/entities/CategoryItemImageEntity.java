package com.code.monks.nukkad.entities;

import com.code.monks.nukkad.enums.ImageUploadStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "category_item_image")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryItemImageEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private ItemEntity item;

//Current Firebase upload status of this image.
    @Enumerated(EnumType.STRING)
    @Column(name = "upload_status", nullable = false)
    private ImageUploadStatus uploadStatus = ImageUploadStatus.PENDING;

//Number of times Firebase upload has been attempted and failed.
    @Column(name = "retry_count", nullable = false)
    private int retryCount = 0;

    // Timestamp of the last Firebase upload attempt.
    @Column(name = "last_synced_at")
    private LocalDateTime lastSyncedAt;





}
