package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.dto.request.SendNotificationRequestDto;
import com.code.monks.nukkad.dto.response.SendNotificationResponseDto;
import com.code.monks.nukkad.services.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/notification")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping(value = "/send", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SendNotificationResponseDto> sendNotification(@RequestBody SendNotificationRequestDto request) {
        SendNotificationResponseDto response = notificationService.sendNotification(
                request.getToken(),
                request.getTitle(),
                request.getBody()
        );
        log.info("send notification response : {}",response);
        return ResponseEntity.ok(response);
    }
}
