package com.code.monks.nukkad.repositories;

import com.code.monks.nukkad.entities.UserNotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotificationEntity , Integer>{
    List<UserNotificationEntity> findByUserId(Long userId);

    // Fetch last 7 days notifications
    List<UserNotificationEntity> findByUserIdAndCreatedAtAfter(Long userId, LocalDateTime createdAt);

    // Delete older than 7 days
    void deleteByUserIdAndCreatedAtBefore(Long userId, LocalDateTime createdAt);
}