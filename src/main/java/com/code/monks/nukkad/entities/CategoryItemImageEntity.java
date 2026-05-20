package com.code.monks.nukkad.entities;
import com.code.monks.nukkad.enums.ImageUploadStatusEnum;
import com.code.monks.nukkad.enums.ImageTypeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "category_item_image")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class CategoryItemImageEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "image_url")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "image_type", nullable = false)
    private ImageTypeEnum imageType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private ItemEntity item;

//Current Firebase upload status of this image.
    @Enumerated(EnumType.STRING)
    @Column(name = "upload_status", nullable = false)
    private ImageUploadStatusEnum uploadStatus = ImageUploadStatusEnum.PENDING;

//Number of times Firebase upload has been attempted and failed.
    @Column(name = "retry_count", nullable = false)
    private int retryCount = 0;

    // Timestamp of the last Firebase upload attempt.
    @Column(name = "last_synced_at")
    private LocalDateTime lastSyncedAt;





}
