
package com.code.monks.nukkad.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "rating")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingEntity extends BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name ="customer_id")
    private CustomerEntity customer;

    @ManyToOne
    @JoinColumn(name = "storekeeper_id")
    private StorekeeperEntity storekeeper;

    @Column(name = "review")
    private String review;

    @Column(name = "rating")
    private int rating;
}

