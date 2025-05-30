package com.code.monks.nukkad.repositories;

import com.code.monks.nukkad.entities.PlaceOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceOrderRepository extends JpaRepository<PlaceOrderEntity,Integer>
{

    Optional<PlaceOrderEntity> findByOrderId(int id);

    boolean existsByOrderId(int id);
}
