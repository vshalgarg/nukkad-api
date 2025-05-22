package com.code.monks.nukkad.repositories;

import com.code.monks.nukkad.entities.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Integer> {
    List<AddressEntity> findAllByCustomerId(int customerId);
    Optional<AddressEntity> findByIdAndCustomerId(Integer id, int customerId);
}

