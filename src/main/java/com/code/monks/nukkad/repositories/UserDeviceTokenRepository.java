package com.code.monks.nukkad.repositories;

import com.code.monks.nukkad.entities.UserDeviceTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDeviceTokenRepository extends JpaRepository<UserDeviceTokenEntity, Integer> {

    Optional<UserDeviceTokenEntity> findByCustomerId(Long customerId);
    Optional<UserDeviceTokenEntity> findByStoreKeeperId(Long storeKeeperId);
}
