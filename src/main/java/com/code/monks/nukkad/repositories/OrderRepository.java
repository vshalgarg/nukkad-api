package com.code.monks.nukkad.repositories;

import com.code.monks.nukkad.entities.OrderEntity;
import com.code.monks.nukkad.enums.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity,Long> {

    List<OrderEntity> findByStatus(StatusEnum status); // Correct

//    List<OrderEntity> findByOrderById();  // Ascending order

    List<OrderEntity> findByCustomerId(Long id);

    Optional<OrderEntity> findById (Long id);

    List<OrderEntity> findByStoreKeeperId(Long storeKeeperId);


    List<OrderEntity> findByStatusAndCreatedAtBetween(StatusEnum status, LocalDateTime startOfDay, LocalDateTime endOfDay);

    Long countByCustomerId(Long customerId);



    @Query("SELECT o FROM OrderEntity o " +
            "WHERE o.customer.id = :customerId " +
            "AND (:status IS NULL OR o.status = :status) " +
            "AND (:start IS NULL OR o.createdAt >= :start) " +
            "AND (:end IS NULL OR o.createdAt <= :end)")
    List<OrderEntity> findCustomerOrdersWithOptionalFilters(
            @Param("customerId") Long customerId,
            @Param("status") StatusEnum status,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);


    @Query("SELECT o FROM OrderEntity o " +
            "WHERE o.storeKeeper.id = :storekeeperId " +
            "AND (:status IS NULL OR o.status = :status) " +
            "AND (:start IS NULL OR o.createdAt >= :start) " +
            "AND (:end IS NULL OR o.createdAt <= :end)")
    List<OrderEntity> findStorekeeperOrdersWithOptionalFilters(
            @Param("storekeeperId") Long storekeeperId,
            @Param("status") StatusEnum status,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

}
