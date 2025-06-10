package com.code.monks.nukkad.repositories;

import com.code.monks.nukkad.entities.OrderEntity;
import com.code.monks.nukkad.enums.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity,Integer> {

   List<OrderEntity> findByStatusEnum(StatusEnum statusEnum);

   List<OrderEntity> findByOrderById();  // Ascending order


   List<OrderEntity> findByCustomerId(int id);
}
