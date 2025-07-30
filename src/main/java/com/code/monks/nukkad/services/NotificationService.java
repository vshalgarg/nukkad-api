package com.code.monks.nukkad.services;

import com.code.monks.nukkad.dto.response.SendNotificationResponseDto;

public interface NotificationService {
    SendNotificationResponseDto sendNotification(String token, String title, String body);
}
