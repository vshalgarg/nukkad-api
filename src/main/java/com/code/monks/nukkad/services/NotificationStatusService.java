package com.code.monks.nukkad.services;

import com.code.monks.nukkad.context.UserContextHolder;
import com.code.monks.nukkad.dto.request.NotificationStatusRequestDTO;
import com.code.monks.nukkad.dto.response.NotificationStatusResponseDTO;
import com.code.monks.nukkad.entities.NotificationStatusEntity;
import com.code.monks.nukkad.enums.NotificationStatusEnum;
import com.code.monks.nukkad.exception.UnhandledException;
import com.code.monks.nukkad.repositories.NotificationStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.code.monks.nukkad.enums.ResponseErrorCodes.UNABLE_TO_SET_NOTIFICATION_ENABLE_STATUS;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationStatusService {

    private final NotificationStatusRepository notificationStatusRepository;

    public NotificationStatusResponseDTO setStatus(NotificationStatusRequestDTO dto){
        try{

        Long userId = UserContextHolder.getUser().getId();
        log.info("[SET NOTIFICATION STATUS] Request received for userId: {} with status: {}", userId, dto.getNotificationStatus());

        NotificationStatusEntity entity = notificationStatusRepository.findByUserId(userId)
                .orElse(new NotificationStatusEntity());

        entity.setUserId(userId);
        entity.setStatus(dto.getNotificationStatus());

        NotificationStatusEntity saved = notificationStatusRepository.save(entity);

        NotificationStatusResponseDTO response = new NotificationStatusResponseDTO();
        response.setUserId(saved.getUserId());
        response.setNotificationStatus(saved.getStatus());

        log.info("[SET NOTIFICATION STATUS] Successfully updated status for userId: {}", userId);
        return response;
    } catch(Exception e){
            log.error("[SET NOTIFICATION STATUS] Failed to set notification status");
            throw new UnhandledException(UNABLE_TO_SET_NOTIFICATION_ENABLE_STATUS,e);
        }
    }

    public void initializeStatusIfAbsent() {
        Long userId = UserContextHolder.getUser().getId();
        Optional<NotificationStatusEntity> existingStatus = notificationStatusRepository.findByUserId(userId);

        if (existingStatus.isPresent()) {
            // Status already set, don't touch it.
            return;
        }
        // Status not present — create default ON status
        NotificationStatusEntity status = new NotificationStatusEntity();
        status.setUserId(userId);
        status.setStatus(NotificationStatusEnum.ON); // default ON
        notificationStatusRepository.save(status);
    }
}
