package com.code.monks.nukkad.repositories;

import com.code.monks.nukkad.entities.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  CategoryRepository extends JpaRepository<CategoryEntity, Long> {

	boolean existsByNameIgnoreCase(String name);
	Page<CategoryEntity> findAllByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
