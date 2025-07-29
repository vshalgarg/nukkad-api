package com.code.monks.nukkad.dto.response;

import com.code.monks.nukkad.enums.NotificationStatusEnum;
import lombok.Data;

@Data
public class NotificationStatusResponseDTO {

    private Long userId;
    private NotificationStatusEnum notificationStatus;
}
