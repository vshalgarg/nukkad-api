//package com.code.monks.nukkad.entities;
//
//import com.code.monks.nukkad.enums.PlaceOrderEnum;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//
//@Entity
//@Table(name = "place_orders_entity")
//@Getter
//@Setter
//public class PlaceOrderEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "item_id", nullable = false)
//    private ItemEntity item;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "order_id", nullable = false)
//    private OrderEntity order;
//
//    @Column(nullable = false)
//    private int quantity;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private PlaceOrderEnum status;
//
//}
