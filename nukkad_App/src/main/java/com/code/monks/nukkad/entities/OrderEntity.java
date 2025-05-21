package com.code.monks.nukkad.entities;

import com.code.monks.nukkad.enums.StatusOrderEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Entity
@Data
public class OrderEntity extends BaseEntity
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
