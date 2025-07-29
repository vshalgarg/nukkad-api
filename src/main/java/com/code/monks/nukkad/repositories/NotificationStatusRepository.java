package com.code.monks.nukkad.repositories;

import com.code.monks.nukkad.entities.NotificationStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationStatusRepository extends JpaRepository <NotificationStatusEntity,Integer>{

    Optional<NotificationStatusEntity> findByUserId(Long userId);
    boolean existsByUserId(Long userId);

}
