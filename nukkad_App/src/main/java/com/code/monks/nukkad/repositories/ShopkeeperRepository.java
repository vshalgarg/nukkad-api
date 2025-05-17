package com.code.monks.nukkad.repositories;

import com.code.monks.nukkad.entities.ShopkeeperEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopkeeperRepository extends JpaRepository<ShopkeeperEntity, Long> {

}
