package com.code.monks.nukkad.services;

import com.code.monks.nukkad.context.UserContextHolder;
import com.code.monks.nukkad.dto.request.NotificationStatusRequestDTO;
import com.code.monks.nukkad.dto.response.NotificationStatusResponseDTO;
import com.code.monks.nukkad.entities.NotificationStatusEntity;
import com.code.monks.nukkad.enums.NotificationStatusEnum;
import com.code.monks.nukkad.repositories.NotificationStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

        boolean exists = notificationStatusRepository.existsByUserId(userId);
        if (!exists) {
            NotificationStatusEntity entity = new NotificationStatusEntity();
            entity.setUserId(userId);
            entity.setStatus(NotificationStatusEnum.ON); // Default status
            notificationStatusRepository.save(entity);
        }
    }


    public boolean isNotificationEnabledForUser() {
        Long userId = UserContextHolder.getUser().getId();
        return notificationStatusRepository.findByUserId(userId)
                .map(e -> e.getStatus() == NotificationStatusEnum.ON)
                .orElse(false); // Default false if no entry (ideally shouldn't happen if initialized)
    }
}
