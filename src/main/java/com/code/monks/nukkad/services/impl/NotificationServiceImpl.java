package com.code.monks.nukkad.services.impl;

import com.code.monks.nukkad.dto.response.SendNotificationResponseDto;
import com.code.monks.nukkad.services.NotificationService;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    @Override
    public SendNotificationResponseDto sendNotification(String token, String title, String body) {
        log.debug("Preparing to send notification... Notification Details -> Token: {}, Title: {}, Body: {}", token, title, body);

        try {
            Message message = Message.builder()
                    .setToken(token)
                    .putData("click_action", "FLUTTER_NOTIFICATION_CLICK")
                    .putData("title", title)
                    .putData("body", body)
                    .setAndroidConfig(AndroidConfig.builder()
                            .setTtl(Duration.ofMinutes(2).toMillis())
                            .setPriority(AndroidConfig.Priority.HIGH)
                            .build())
                    .build();

            log.debug("Built Firebase Message: {}", message);

            String messageId = FirebaseMessaging.getInstance().send(message);
            log.info("Notification sent to token {}: {}", token, messageId);
            return new SendNotificationResponseDto(true, "Notification sent successfully", messageId);

        } catch (Exception e) {
            log.error("Failed to send notification to token {}: {}", token, e.getMessage(), e);
            return new SendNotificationResponseDto(false, "Failed to send notification: " + e.getMessage(), null);
        }
    }
}
