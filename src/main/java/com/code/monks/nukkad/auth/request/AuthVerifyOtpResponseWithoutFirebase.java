package com.code.monks.nukkad.auth.request;

import com.code.monks.nukkad.enums.AuthUserStatusEnum;
import com.code.monks.nukkad.enums.UserStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

    import java.util.List;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public class AuthVerifyOtpResponseWithoutFirebase {

        private Long userId;
        private String username;
        private List<String> roles;
        private List<String> permissions;
        private String token;
        private AuthUserStatusEnum status;
    }
