package com.neepanlokInfotech.nukkad_App.repositories;

import com.neepanlokInfotech.nukkad_App.entities.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity,Long> {

}
