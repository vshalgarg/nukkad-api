package com.code.monks.nukkad.entities;


import com.code.monks.nukkad.enums.AddressLabel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "addresses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    private String addressLine1;

    private String addressLine2;

    private String landmark;

    private String city;

    private String state;

    private String pincode;

    @Enumerated(EnumType.STRING)
    private AddressLabel label;

//    private boolean isSelected;

    private Long customerId;


}
