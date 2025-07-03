package com.code.monks.nukkad.entities;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "storekeeper_qr_code")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StorekeeperQrCodeEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "qr_image_url", nullable = false)
    private String qrImageUrl;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storekeeper_id", nullable = false)
    private StorekeeperEntity storekeeper;
}
