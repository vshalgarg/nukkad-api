package com.neepanlokInfotech.nukkad_App.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderResponseDTO {

    private Long id;
    private String trackingNumber;
    private int quantity;
    private String status;
    private Long orderId;
    private LocalDateTime orderDate;
    private String dayName;
    private Long shopKeeperId;

}
