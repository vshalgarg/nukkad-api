package com.code.monks.nukkad.repositories;

import com.code.monks.nukkad.entities.StorekeeperEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StorekeeperRepository extends JpaRepository<StorekeeperEntity, Long> {

    boolean existsByStoreQrId(String storeQrId);
    Optional<StorekeeperEntity> findByGstNum(String gstNum);
    Optional<StorekeeperEntity> findByAddressLine1(String addressLine1);
    Optional<StorekeeperEntity> findByStoreQrId(String storeQrId);

}
