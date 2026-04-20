package com.code.monks.nukkad.repositories;

import java.util.Optional;
import com.code.monks.nukkad.entities.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
    Page<ItemEntity> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    @Query("SELECT i FROM item i JOIN i.categories c WHERE c.id = :categoryId")
    Page<ItemEntity> findItemsByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    @Query("SELECT i.name, c.name FROM item i JOIN i.categories c WHERE i.name IN :names")
    List<Object[]> findExistingProductsWithCategory(@Param("names") Set<String> names);

    @Query("SELECT i FROM item i "
            + "LEFT JOIN FETCH i.categories "
            + "LEFT JOIN FETCH i.images "
            + "WHERE i.name = :name")
    Optional<ItemEntity> findByNameWithCategoriesAndImages(
            @Param("name") String name);

    @Query("SELECT i.name FROM item i WHERE i.name IN :names")
    List<String> findExistingNames(@Param("names") Set<String> names);
    boolean existsByName(String name);
}
