package com.code.monks.nukkad.services;

import com.code.monks.nukkad.context.UserContextHolder;
import com.code.monks.nukkad.dto.response.DeleteAllUserNotificationsResponseDTO;
import com.code.monks.nukkad.dto.response.UserNotificationResponseDTO;
import com.code.monks.nukkad.entities.UserNotificationEntity;
import com.code.monks.nukkad.exception.UnhandledException;
import com.code.monks.nukkad.repositories.UserNotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.code.monks.nukkad.enums.ResponseErrorCodes.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserNotificationService {

    private final UserNotificationRepository userNotificationRepository;

    public void saveNotification(Long userId, String title, String message) {
        try {
            log.info("Saving notification for userId: {}, title: {}", userId, title);

            UserNotificationEntity notification = UserNotificationEntity.builder()
                    .userId(userId)
                    .title(title)
                    .message(message)
                    .build();

            userNotificationRepository.save(notification);
            log.info("Notification saved successfully for userId: {}", userId);
        } catch (Exception e) {
            log.error("Failed to save notification for userId: {}", userId, e);
            throw new UnhandledException(ENABLE_TO_SAVE_NOTIFICATION, e);
        }
    }

    public List<UserNotificationResponseDTO> getNotificationsByUserId() {
        try {
            Long userId = UserContextHolder.getUser().getId();
            log.info("Fetching notifications for userId: {}", userId);

            // Define the cutoff date as 7 days ago
            LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

            // Delete old notifications
            userNotificationRepository.deleteByUserIdAndCreatedAtBefore(userId, sevenDaysAgo);
            log.info("Deleted notifications older than 7 days for userId: {}", userId);

            // Fetch recent notifications
            List<UserNotificationEntity> notifications = userNotificationRepository
                    .findByUserIdAndCreatedAtAfter(userId, sevenDaysAgo);

            log.info("Found {} notifications for userId: {}", notifications.size(), userId);

            return notifications.stream()
                    .map(entity -> UserNotificationResponseDTO.builder()
                            .title(entity.getTitle())
                            .message(entity.getMessage())
                            .createdAt(entity.getCreatedAt())
                            .build())
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Failed to fetch notifications", e);
            throw new UnhandledException(ENABLE_TO_FETCH_NOTIFICATIONS, e);
        }
    }

    public DeleteAllUserNotificationsResponseDTO deleteAllUserNotifications() {
        try {
            Long userId = UserContextHolder.getUser().getId();
            log.info("Deleting all notifications for userId: {}", userId);

            List<UserNotificationEntity> notifications = userNotificationRepository.findByUserId(userId);
            userNotificationRepository.deleteAll(notifications);

            return DeleteAllUserNotificationsResponseDTO.builder()
                    .message("All notifications are cleared")
                    .build();

        } catch (Exception e) {
            log.error("Failed to delete notifications", e);
            throw new UnhandledException(UNABLE_TO_ALL_NOTIFICATIONS, e);
        }
    }
}