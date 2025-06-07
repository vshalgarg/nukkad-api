package com.code.monks.nukkad.entities;

import com.code.monks.nukkad.converter.StatusEnumConverter;
import com.code.monks.nukkad.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="myOrder_entity")
@Data
public class MyOrderEntity extends BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int orderId;
    private int customerId;
    private int storeKeeperId;

//    @Convert(converter = StatusEnumConverter.class)
    private String statusEnum;

}
