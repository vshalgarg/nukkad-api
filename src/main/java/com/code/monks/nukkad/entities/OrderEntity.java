
package com.code.monks.nukkad.entities;
import com.code.monks.nukkad.converter.StatusConverter;
import com.code.monks.nukkad.enums.Status;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "store_keeper_note")
    private String note;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> orderItems = new ArrayList<>();

}
