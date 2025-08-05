package com.code.monks.nukkad.services;

import com.code.monks.nukkad.entities.UserDeviceTokenEntity;
import com.code.monks.nukkad.enums.RoleEnum;
import com.code.monks.nukkad.repositories.UserDeviceTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDeviceTokenService {

    private final UserDeviceTokenRepository userDeviceTokenRepository;

    public Optional<String> getDeviceTokenForUser(Long userId, RoleEnum role) {
        if (role == RoleEnum.CUSTOMER) {
            return userDeviceTokenRepository.findByCustomerId(userId)
                    .map(UserDeviceTokenEntity::getDeviceToken);
        } else if (role == RoleEnum.STOREKEEPER) {
            return userDeviceTokenRepository.findByStoreKeeperId(userId)
                    .map(UserDeviceTokenEntity::getDeviceToken);
        } else {
            return Optional.empty();
        }
    }
}
