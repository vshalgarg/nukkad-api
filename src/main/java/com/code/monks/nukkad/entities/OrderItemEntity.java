package com.code.monks.nukkad.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "order_item")
@Data
public class OrderItemEntity extends  BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to Order
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity orders;

    // Link to Item
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private ItemEntity item;

    @Column(name = "item_Name")
    private String itemName;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @Column(name = "unit")
    private String unit;

    @Column(name = "price")
    private Double price;
}
