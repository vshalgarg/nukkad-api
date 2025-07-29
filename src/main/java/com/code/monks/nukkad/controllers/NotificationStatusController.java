package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.constants.UrlConstants;
import com.code.monks.nukkad.dto.request.NotificationStatusRequestDTO;
import com.code.monks.nukkad.dto.response.NotificationStatusResponseDTO;
import com.code.monks.nukkad.services.NotificationStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(UrlConstants.NOTIFICATION.BASE)
@RequiredArgsConstructor
public class NotificationStatusController {

    private final NotificationStatusService notificationStatusService;

    @PostMapping(UrlConstants.NOTIFICATION.SET_STATUS)
    public ResponseEntity<NotificationStatusResponseDTO> setStatus(@RequestBody NotificationStatusRequestDTO request) {
        log.info("Setting notification status to {}", request.getNotificationStatus());
        NotificationStatusResponseDTO response = notificationStatusService.setStatus(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping(UrlConstants.NOTIFICATION.CHECK_STATUS)
    public ResponseEntity<Boolean> isNotificationEnabled() {
        boolean enabled = notificationStatusService.isNotificationEnabledForUser();
        return ResponseEntity.ok(enabled);
    }

}
