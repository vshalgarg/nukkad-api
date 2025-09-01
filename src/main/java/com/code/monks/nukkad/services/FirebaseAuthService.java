package com.code.monks.nukkad.services;

import com.code.monks.nukkad.dto.request.FirebaseTokenRequestDTO;
import com.code.monks.nukkad.dto.response.FirebaseLoginResponseDTO;
import com.code.monks.nukkad.entities.UserDeviceTokenEntity;
import com.code.monks.nukkad.enums.RoleEnum;
import com.code.monks.nukkad.repositories.CustomerRepository;
import com.code.monks.nukkad.repositories.StorekeeperRepository;
import com.code.monks.nukkad.repositories.UserDeviceTokenRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FirebaseAuthService {

    private final CustomerRepository customerRepository;
    private final StorekeeperRepository storekeeperRepository;
    private final UserDeviceTokenRepository userDeviceTokenRepository;

    public FirebaseLoginResponseDTO verifyFirebaseToken(FirebaseTokenRequestDTO request) throws Exception {
        FirebaseToken decoded = FirebaseAuth.getInstance().verifyIdToken(request.getIdToken());
        String uid = decoded.getUid();

        UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);
        String phone = userRecord.getPhoneNumber();

        log.info("[FIREBASE VERIFY] uid={}, phone={}", uid, phone);

        // Default role assumption
        // Later you can map roles from DB or claims
        String role = RoleEnum.CUSTOMER.name();
        boolean firstTimeLogin = !customerRepository.existsById(Long.parseLong(uid));

        // Save/update device token
        if (request.getDeviceToken() != null) {
            Optional<UserDeviceTokenEntity> existingTokenOpt = userDeviceTokenRepository.findByCustomerId(Long.parseLong(uid));
            UserDeviceTokenEntity tokenEntity = existingTokenOpt.orElse(new UserDeviceTokenEntity());
            tokenEntity.setCustomerId(Long.parseLong(uid));
            tokenEntity.setDeviceToken(request.getDeviceToken());
            userDeviceTokenRepository.save(tokenEntity);
        }

        return new FirebaseLoginResponseDTO(uid, phone, Collections.singletonList(role), firstTimeLogin, request.getIdToken());
    }

}
