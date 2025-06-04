package com.code.monks.nukkad.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CustomerEntity extends UserEntity {

    @Column(name = "email_id")
    private String email;

    @Column(name = "address_Line1")
    private String addressLine1;

    @Column(name = "address_Line2")
    private String addressLine2;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "pincode")
    private String pincode;

    @Column(name = "dob")
    private String dob;


}
