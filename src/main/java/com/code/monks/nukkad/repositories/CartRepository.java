package com.code.monks.nukkad.repositories;

import com.code.monks.nukkad.entities.CartEntity;
import com.code.monks.nukkad.entities.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {
    Optional<CartEntity> findByCustomer(CustomerEntity customer);
}
