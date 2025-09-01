package com.code.monks.nukkad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FirebaseLoginResponseDTO {
    private String uid;
    private String phoneNumber;
    private List<String> roles;
    private boolean firstTimeLogin;
    private String firebaseToken; // same ID token or custom backend JWT (your choice)
}
