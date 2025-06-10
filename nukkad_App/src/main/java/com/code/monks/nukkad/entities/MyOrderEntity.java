//package com.code.monks.nukkad.entities;
//
//import com.code.monks.nukkad.converter.OrderStatusEnumConverter;
//import com.code.monks.nukkad.enums.OrderStatusEnum;
//import jakarta.persistence.*;
//import lombok.Data;
//
//@Entity
//@Table(name="my_order_entity")
//@Data
//public class MyOrderEntity extends BaseEntity
//{
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//    private int orderId;
//    private int customerId;
//    private int storeKeeperId;
//
//    @Convert(converter = OrderStatusEnumConverter.class)
//    private OrderStatusEnum orderStatusEnum;
//
//}
