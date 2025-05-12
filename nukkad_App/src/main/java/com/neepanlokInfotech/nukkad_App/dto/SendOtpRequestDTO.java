package com.neepanlokInfotech.nukkad_App.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SendOtpRequestDTO {

    public String name;
    private String mobileNumber;
}
