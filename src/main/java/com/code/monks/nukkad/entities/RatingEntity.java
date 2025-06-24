
package com.code.monks.nukkad.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rating")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name ="customer_id",nullable = false)
    private CustomerEntity customer;

    @ManyToOne
    @JoinColumn(name = "storekeeper_id",nullable = false)
    private StorekeeperEntity storekeeper;

    @Column(nullable = false)
    private String review;

    @Column(nullable = false)
    private int rating;
}

