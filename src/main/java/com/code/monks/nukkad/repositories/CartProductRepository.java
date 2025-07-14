package com.code.monks.nukkad.repositories;

import com.code.monks.nukkad.entities.CartProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartProductRepository extends JpaRepository<CartProductEntity, Long> {
    List<CartProductEntity> findByCustomerId(Long customerId);
    Optional<CartProductEntity> findByCustomerIdAndItemId(Long customerId, Long itemId);

}
