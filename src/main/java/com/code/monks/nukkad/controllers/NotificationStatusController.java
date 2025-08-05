package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.constants.UrlConstants;
import com.code.monks.nukkad.dto.request.NotificationStatusRequestDTO;
import com.code.monks.nukkad.dto.response.DeleteAllUserNotificationsResponseDTO;
import com.code.monks.nukkad.dto.response.NotificationStatusResponseDTO;
import com.code.monks.nukkad.dto.response.UserNotificationResponseDTO;
import com.code.monks.nukkad.services.NotificationStatusService;
import com.code.monks.nukkad.services.UserNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.code.monks.nukkad.constants.UrlConstants.NOTIFICATION.DELETE_ALL_NOTIFICATIONS;
import static com.code.monks.nukkad.constants.UrlConstants.NOTIFICATION.GET_NOTIFICATIONS;

@Slf4j
@RestController
@RequestMapping(UrlConstants.NOTIFICATION.BASE)
@RequiredArgsConstructor
public class NotificationStatusController {

    private final NotificationStatusService notificationStatusService;
    private final UserNotificationService userNotificationService;

    @PostMapping(UrlConstants.NOTIFICATION.SET_STATUS)
    public ResponseEntity<NotificationStatusResponseDTO> setStatus(@RequestBody NotificationStatusRequestDTO request) {
        log.info("Setting notification status to {}", request.getNotificationStatus());
        NotificationStatusResponseDTO response = notificationStatusService.setStatus(request);
        return ResponseEntity.ok(response);
    }
    @GetMapping(GET_NOTIFICATIONS)
   public ResponseEntity<List<UserNotificationResponseDTO>> getMyNotifications() {
                log.info("Request received to fetch notifications for the current user");
                List<UserNotificationResponseDTO> notifications = userNotificationService.getNotificationsByUserId();
                log.info("Returning {} notifications", notifications.size());
                return ResponseEntity.ok(notifications);
           }

    @DeleteMapping(DELETE_ALL_NOTIFICATIONS)
    public ResponseEntity<DeleteAllUserNotificationsResponseDTO> deleteAllUserNotifications() {
                log.info("Request received to delete all notifications for current user");
                DeleteAllUserNotificationsResponseDTO response = userNotificationService.deleteAllUserNotifications();
                log.info("Successfully deleted all notifications for current user");
                return ResponseEntity.ok(response);
            }
}
