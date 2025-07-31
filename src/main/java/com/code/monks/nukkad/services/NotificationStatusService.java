package com.code.monks.nukkad.services;

import com.code.monks.nukkad.context.UserContextHolder;
import com.code.monks.nukkad.dto.request.NotificationStatusRequestDTO;
import com.code.monks.nukkad.dto.response.NotificationStatusResponseDTO;
import com.code.monks.nukkad.entities.NotificationStatusEntity;
import com.code.monks.nukkad.enums.NotificationStatusEnum;
import com.code.monks.nukkad.repositories.NotificationStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationStatusService {

    private final NotificationStatusRepository notificationStatusRepository;

    public NotificationStatusResponseDTO setStatus(NotificationStatusRequestDTO dto){
        Long userId = UserContextHolder.getUser().getId();

        NotificationStatusEntity entity = notificationStatusRepository.findByUserId(userId)
                .orElse(new NotificationStatusEntity());

        entity.setUserId(userId);
        entity.setStatus(dto.getNotificationStatus());

        NotificationStatusEntity saved = notificationStatusRepository.save(entity);

        NotificationStatusResponseDTO response = new NotificationStatusResponseDTO();
        response.setUserId(saved.getUserId());
        response.setNotificationStatus(saved.getStatus());
        return response;
    }

    public void initializeStatusIfAbsent() {
        Long userId = UserContextHolder.getUser().getId();
        Optional<NotificationStatusEntity> existingStatus = notificationStatusRepository.findByUserId(userId);

        if (existingStatus.isPresent()) {
            // Status already set, don't touch it (user has ON/OFF preference)
            return;
        }

        // Status not present — create default ON status
        NotificationStatusEntity status = new NotificationStatusEntity();
        status.setUserId(userId);
        status.setStatus(NotificationStatusEnum.ON); // default ON
        notificationStatusRepository.save(status);
    }


}
