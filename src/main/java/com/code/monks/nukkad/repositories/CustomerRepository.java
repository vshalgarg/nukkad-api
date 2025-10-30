package com.code.monks.nukkad.repositories;

import com.code.monks.nukkad.entities.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity,Long> {

    Optional<CustomerEntity> findByMobileNumber(String mobileNumber);
    Optional<CustomerEntity> findByEmail(String email);

    Optional<CustomerEntity> findById(Long customerId);
}
