package com.neepanlokInfotech.nukkad_App.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopkeeperEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String storeNumber;
    private String gstIn;
    private String Address;
    private String City;

    @ElementCollection
    private List<String> pictures;
}
