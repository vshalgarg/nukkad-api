package com.code.monks.nukkad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserNotificationResponseDTO {
    private String title;
    private String message;
    private LocalDateTime createdAt;
}