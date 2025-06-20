package com.code.monks.nukkad.repositories;

import com.code.monks.nukkad.entities.OrderEntity;
import com.code.monks.nukkad.enums.Status;
import org.springframework.beans.PropertyValues;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity,Long> {

    List<OrderEntity> findByStatus(Status status); // ✅ Correct

//    List<OrderEntity> findByOrderById();  // Ascending order

    List<OrderEntity> findByCustomerId(Long id);

    Optional<OrderEntity> findById(Long id);

    List<OrderEntity> findByStoreKeeperId(Long storeKeeperId);

    // List<OrderEntity> findByStatusEnumAndCreatedAtBetween(...);

    List<OrderEntity> findByStatusAndCreatedAtBetween(Status status, LocalDateTime startOfDay, LocalDateTime endOfDay);

//    PropertyValues findByStoreKeeperId(Long storeKeeperId);

}
