package com.code.monks.nukkad.repositories;

import com.code.monks.nukkad.entities.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
    List<CartItemEntity> findByCustomerId(Long customerId);
    Optional<CartItemEntity> findByCustomerIdAndItemId(Long customerId, Long itemId);

}
