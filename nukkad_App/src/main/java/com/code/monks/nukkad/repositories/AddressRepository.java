package com.code.monks.nukkad.repositories;

import com.code.monks.nukkad.entities.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {

    // Find a single address by id and customer id
    Optional<AddressEntity> findByIdAndCustomer_Id(Long id, Long customerId);

    // Find a single address by id and storekeeper id
    Optional<AddressEntity> findByIdAndStorekeeper_Id(Long id, Long storekeeperId);

    // Find all addresses for a customer
    List<AddressEntity> findAllByCustomer_Id(Long customerId);

    // Find all addresses for a storekeeper
    List<AddressEntity> findAllByStorekeeper_Id(Long storekeeperId);
}


