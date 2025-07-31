package com.code.monks.nukkad.entities;

import com.code.monks.nukkad.enums.PlaceOrderEnum;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class PlaceOrderEntity extends BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private Long orderId;
    private Long itemId;
    private int quantity;

    @Enumerated(EnumType.STRING)
    private PlaceOrderEnum placeOrderEnum;
}
