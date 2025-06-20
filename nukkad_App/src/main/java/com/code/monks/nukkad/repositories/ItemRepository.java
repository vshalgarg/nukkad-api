package com.code.monks.nukkad.repositories;

import com.code.monks.nukkad.entities.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
    List<ItemEntity> findByNameContainingIgnoreCase(String keyword);


}
