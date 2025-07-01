package com.code.monks.nukkad.repositories;

import com.code.monks.nukkad.entities.StorekeeperQrCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorekeeperQrCodeRepository extends JpaRepository<StorekeeperQrCodeEntity, Long> {
    int countByStorekeeperId(Long storekeeperId);
    boolean existsByStorekeeperIdAndIsDefaultTrue(Long storekeeperId);
    List<StorekeeperQrCodeEntity> findByStorekeeperId(Long storekeeperId);
}