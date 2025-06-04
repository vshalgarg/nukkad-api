package com.code.monks.nukkad.entities;

import com.code.monks.nukkad.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public abstract class UserEntity {
    public abstract RoleEnum getRole();
}
