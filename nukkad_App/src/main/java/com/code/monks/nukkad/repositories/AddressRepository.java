//package com.code.monks.nukkad.repositories;
//
//import com.code.monks.nukkad.entities.AddressEntity;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
//
//    // Efficiently fetch all addresses for a specific customer
//    List<AddressEntity> findAllByCustomerId(Long customerId);
//
//    // Efficiently fetch address by ID and customerId to ensure ownership
//    Optional<AddressEntity> findByIdAndCustomerId(Long id, Long customerId);
//}
//
//
