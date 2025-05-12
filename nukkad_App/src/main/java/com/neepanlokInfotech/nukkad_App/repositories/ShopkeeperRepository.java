package com.neepanlokInfotech.nukkad_App.repositories;

import com.neepanlokInfotech.nukkad_App.entities.ShopkeeperEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopkeeperRepository extends JpaRepository<ShopkeeperEntity,Long> {
}
