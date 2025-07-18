
package com.code.monks.nukkad.entities;
import com.code.monks.nukkad.converter.StatusConverter;
import com.code.monks.nukkad.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="orders")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class  OrderEntity extends BaseEntity {
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
    private StatusEnum status;

    @Column(name = "store_keeper_note")
    private String storeKeeperNote;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> orderItems = new ArrayList<>();

}
