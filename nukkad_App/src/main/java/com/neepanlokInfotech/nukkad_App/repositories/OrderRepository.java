package com.neepanlokInfotech.nukkad_App.repositories;

import com.neepanlokInfotech.nukkad_App.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity,Long> {
   List<OrderEntity> findByStatusIgnoreCase(String status);
}
