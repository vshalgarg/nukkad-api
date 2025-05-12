package com.neepanlokInfotech.nukkad_App.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;
@Entity
@Data
public class OtpEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mobileNumber;
    private String otp;
    private String message;
    private LocalDateTime createdAt;
    private boolean verified;

}

