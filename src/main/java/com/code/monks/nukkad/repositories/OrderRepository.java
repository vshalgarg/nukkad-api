package com.code.monks.nukkad.repositories;

import com.code.monks.nukkad.entities.OrderEntity;
import com.code.monks.nukkad.enums.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public interface OrderRepository extends JpaRepository<OrderEntity,Long> {

    Optional<OrderEntity> findById (Long id);

    List<OrderEntity> findByStoreKeeperId(Long storeKeeperId);

    @Query("""
SELECT o FROM OrderEntity o
JOIN o.orderItems oi
WHERE o.customer.id = :customerId
  AND (
      (:status IS NOT NULL AND o.status = :status)
      OR (:status IS NULL AND (:minPrice IS NOT NULL OR :maxPrice IS NOT NULL) AND 
         (o.status = com.code.monks.nukkad.enums.StatusEnum.DISPATCHED OR o.status = com.code.monks.nukkad.enums.StatusEnum.DELIVERED))
      OR (:status IS NULL AND :minPrice IS NULL AND :maxPrice IS NULL)
  )
  AND (:start IS NULL OR o.createdAt >= :start)
  AND (:end IS NULL OR o.createdAt <= :end)
GROUP BY o.id
HAVING 
  (:minPrice IS NULL OR SUM(oi.price) >= :minPrice) AND
  (:maxPrice IS NULL OR SUM(oi.price) <= :maxPrice)
""")
    List<OrderEntity> findCustomerOrdersWithFilters(
            @Param("customerId") Long customerId,
            @Param("status") StatusEnum status,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice
    );

    @Query("""
SELECT o FROM OrderEntity o
JOIN o.orderItems oi
WHERE o.storeKeeper.id = :storekeeperId
  AND (
      (:status IS NOT NULL AND o.status = :status)
      OR (:status IS NULL AND (:minPrice IS NOT NULL OR :maxPrice IS NOT NULL) AND 
         (o.status = com.code.monks.nukkad.enums.StatusEnum.DISPATCHED OR o.status = com.code.monks.nukkad.enums.StatusEnum.DELIVERED))
      OR (:status IS NULL AND :minPrice IS NULL AND :maxPrice IS NULL)
  )
  AND (:start IS NULL OR o.createdAt >= :start)
  AND (:end IS NULL OR o.createdAt <= :end)
GROUP BY o.id
HAVING 
  (:minPrice IS NULL OR SUM(oi.price) >= :minPrice) AND
  (:maxPrice IS NULL OR SUM(oi.price) <= :maxPrice)
""")
    List<OrderEntity> findStorekeeperOrdersWithFilters(
            @Param("storekeeperId") Long storekeeperId,
            @Param("status") StatusEnum status,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice
    );
}
