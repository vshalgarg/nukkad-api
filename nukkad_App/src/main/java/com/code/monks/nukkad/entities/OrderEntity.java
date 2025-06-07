package com.code.monks.nukkad.entities;

import com.code.monks.nukkad.converter.StatusEnumConverter;
import com.code.monks.nukkad.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class OrderEntity extends BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name ="cart_id")
    private CartItemEntity cart;

    @ManyToOne
    @JoinColumn(name ="customer_id")
    private CustomerEntity customer;

    @ManyToOne
    @JoinColumn(name ="deliveryAddress_id")
    private AddressEntity deliveryAddress;

    @ManyToOne
    @JoinColumn(name="storeKeeper_id")
    private StorekeeperEntity storeKeeper;

    @Convert(converter = StatusEnumConverter.class)
    private StatusEnum statusEnum;


}
