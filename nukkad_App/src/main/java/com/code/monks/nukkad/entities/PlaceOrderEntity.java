//package com.code.monks.nukkad.entities;
//
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
//    private int cartid;
//
//
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "address_id",nullable = false)
//    private AddressEntity address;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "storeKeeper_id", nullable = false)
//    private StorekeeperEntity storekeeper;
//
//}
