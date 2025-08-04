package com.code.monks.nukkad.repositories;

import com.code.monks.nukkad.entities.UserNotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotificationEntity , Integer>{

}
