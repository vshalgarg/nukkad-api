package com.code.monks.nukkad.repositories;

import com.code.monks.nukkad.entities.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
    Page<ItemEntity> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    @Query("SELECT i FROM item i JOIN i.categories c WHERE c.id = :categoryId")
    Page<ItemEntity> findItemsByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);
}
