package com.code.monks.nukkad.auth.response;

import com.code.monks.nukkad.enums.UserStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthAdminRegisterResponseDTO {
    private UserStatusEnum status;
}

