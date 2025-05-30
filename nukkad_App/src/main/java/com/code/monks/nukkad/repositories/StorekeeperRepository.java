package com.code.monks.nukkad.repositories;

import com.code.monks.nukkad.entities.StorekeeperEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorekeeperRepository extends JpaRepository<StorekeeperEntity, Long> {

}
