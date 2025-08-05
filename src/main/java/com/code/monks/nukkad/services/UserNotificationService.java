package com.code.monks.nukkad.services;

import com.code.monks.nukkad.entities.UserNotificationEntity;
import com.code.monks.nukkad.repositories.UserNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserNotificationService {
    private final UserNotificationRepository userNotificationRepository;

    public void saveNotification(Long userId, String title, String message) {
        UserNotificationEntity notification = UserNotificationEntity.builder()
                .userId(userId)
                .title(title)
                .message(message)
                .build();

        userNotificationRepository.save(notification);
    }
}
