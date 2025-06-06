package com.code.monks.nukkad.dto;

import com.code.monks.nukkad.enums.RoleEnum;
import lombok.Data;

@Data
public class User {
    private Long id;
    private RoleEnum role;
}
