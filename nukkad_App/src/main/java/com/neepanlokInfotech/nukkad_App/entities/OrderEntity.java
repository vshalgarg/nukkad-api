package com.neepanlokInfotech.nukkad_App.entities;

import com.neepanlokInfotech.nukkad_App.enums.StatusOrderEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Entity
@Data
public class OrderEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String trackingNumber;
    private int quantity;
    private String status;
    private Long orderId;
    private LocalDateTime orderDate;
    private String dayName;
    private Long shopKeeperId;

    @Enumerated(EnumType.STRING)
    private StatusOrderEnum statusOrderEnum;


}
