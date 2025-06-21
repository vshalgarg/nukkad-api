package com.code.monks.nukkad.repositories;

import com.code.monks.nukkad.entities.AddressEntity;
import com.code.monks.nukkad.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
    List<AddressEntity> findByCustomerId(Long customerId);
    Optional<AddressEntity> findByIdAndCustomerId(Long id, Long customerId);

    Optional<AddressEntity> findByCustomerIdAndIsDefaultTrue(Long customerId);
}
