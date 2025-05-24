package com.code.monks.nukkad.entities;

import com.code.monks.nukkad.enums.ItemOrderEnum;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ItemOrderEntity extends BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int quantity;
    private String description;
    private Long phoneNumber;


    @Enumerated(EnumType.STRING)
    private ItemOrderEnum itemOrderEnum;
}
