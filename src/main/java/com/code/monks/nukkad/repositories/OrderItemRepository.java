package com.code.monks.nukkad.repositories;

import com.code.monks.nukkad.entities.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {

    @Query("SELECT SUM(oi.quantity) FROM OrderItemEntity oi WHERE oi.orders.id = :orderId")
    Long findTotalQuantityByOrderId(@Param("orderId") Long orderId);


}
