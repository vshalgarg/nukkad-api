package com.code.monks.nukkad.entities;
import com.code.monks.nukkad.converter.StatusConverter;
import com.code.monks.nukkad.enums.Status;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="orders")
@Data
public class OrderEntity extends BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name ="cart_id")
    private CartItemEntity cart;

    @ManyToOne
    @JoinColumn(name ="customer_id")
    private CustomerEntity customer;

    @ManyToOne
    @JoinColumn(name ="delivery_address_id")
    private AddressEntity deliveryAddress;

    @ManyToOne
    @JoinColumn(name="store_keeper_id")
    private StorekeeperEntity storeKeeper;

   @Convert(converter = StatusConverter.class)
    private Status status;


}
