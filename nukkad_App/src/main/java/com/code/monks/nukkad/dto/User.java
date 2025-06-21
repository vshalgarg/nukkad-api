package com.code.monks.nukkad.dto;

import com.code.monks.nukkad.enums.RoleEnum;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class User {
    private Long id;
    private String mobileNumber;
    private List<RoleEnum> roles = new ArrayList<>();

    public boolean hasRole(RoleEnum role) {
        return roles != null && roles.contains(role);
    }
}
