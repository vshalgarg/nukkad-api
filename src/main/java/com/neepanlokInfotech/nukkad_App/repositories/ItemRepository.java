package com.neepanlokInfotech.nukkad_App.repositories;

import com.neepanlokInfotech.nukkad_App.entities.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity,Long>{


}
