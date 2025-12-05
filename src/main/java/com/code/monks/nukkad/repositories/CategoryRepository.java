package com.code.monks.nukkad.repositories;

import com.code.monks.nukkad.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface  CategoryRepository extends JpaRepository<CategoryEntity, Long> {

	boolean existsByNameIgnoreCase(String name);
	List<CategoryEntity> findAllByNameContainingIgnoreCase(String keyword);
	Optional<CategoryEntity> findByName(String name);
	List<CategoryEntity> findAllByNameIn(Set<String> names);
}
