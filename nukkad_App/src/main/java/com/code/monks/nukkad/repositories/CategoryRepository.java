package com.code.monks.nukkad.repositories;

import com.code.monks.nukkad.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface  CategoryRepository extends JpaRepository<CategoryEntity, Long> {

	boolean existsByNameIgnoreCase(String name); // Checks case-insensitive duplicates
	List<CategoryEntity> findByNameContainingIgnoreCase(String keyword);


}
