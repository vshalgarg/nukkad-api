package com.code.monks.nukkad.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SendNotificationRequestDto {

    private String token;
    private String title;
    private String body;
}
