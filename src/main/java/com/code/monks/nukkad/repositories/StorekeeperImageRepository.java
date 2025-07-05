package com.code.monks.nukkad.repositories;

import com.code.monks.nukkad.entities.StorekeeperEntity;
import com.code.monks.nukkad.entities.StorekeeperImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorekeeperImageRepository extends JpaRepository<StorekeeperImageEntity,Long> {

    List<StorekeeperImageEntity> findByStorekeeperId(Long StorekeeperId);
}
