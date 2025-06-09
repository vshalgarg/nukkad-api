//package com.code.monks.nukkad.repositories;
//
//import com.code.monks.nukkad.entities.OrderEntity;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//
//public interface OrderRepository extends JpaRepository<OrderEntity,Integer> {
//
//   List<OrderEntity> findByStatusEnum(String status);
//
//   List<OrderEntity> findByTrackingNumberIgnoreCase(String trackingNumber);
//}
