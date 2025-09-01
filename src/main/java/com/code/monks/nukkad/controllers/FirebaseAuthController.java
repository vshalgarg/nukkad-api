package com.code.monks.nukkad.controllers;

import com.code.monks.nukkad.dto.request.FirebaseTokenRequestDTO;
import com.code.monks.nukkad.dto.response.FirebaseLoginResponseDTO;
import com.code.monks.nukkad.services.FirebaseAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class FirebaseAuthController {

    private final FirebaseAuthService firebaseAuthService;

    @PostMapping("/verify-token")
    public ResponseEntity<FirebaseLoginResponseDTO> verifyToken(@RequestBody FirebaseTokenRequestDTO request) {
        try {
            FirebaseLoginResponseDTO response = firebaseAuthService.verifyFirebaseToken(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }

}
