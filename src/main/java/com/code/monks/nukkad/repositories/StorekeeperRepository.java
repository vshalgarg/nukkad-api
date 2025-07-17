package com.code.monks.nukkad.repositories;

import com.code.monks.nukkad.entities.StorekeeperEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StorekeeperRepository extends JpaRepository<StorekeeperEntity, Long> {
    boolean existsByGstNum(String gstNum);
    boolean existsByMobileNumber(String mobileNumber);
    boolean existsByStoreQrId(String storeQrId);
    boolean existsByAddressLine1(String addressLine1);
    Optional<StorekeeperEntity> findByStoreQrId(String storeQrId);
}
