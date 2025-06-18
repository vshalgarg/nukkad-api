package com.code.monks.nukkad.repositories;

import com.code.monks.nukkad.entities.AddressEntity;
import com.code.monks.nukkad.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
    List<AddressEntity> findByUserId(Long userId);
}
