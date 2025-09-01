package com.code.monks.nukkad.dto.request;

import lombok.Data;

@Data
public class FirebaseTokenRequestDTO {
    private String idToken;     // Firebase ID Token from frontend
    private String deviceToken; // Optional device token (FCM)
}
