package com.code.monks.nukkad.entities;

import com.code.monks.nukkad.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class CustomerEntity extends UserEntity {

    @EqualsAndHashCode.Include
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email_id")
    private String email;

    @Column(name = "dob")
    private String dob;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "profile_image")
    private String profileImage;

    @Override
    public RoleEnum getRole(){
        return RoleEnum.CUSTOMER;
    }

    @ManyToMany
    @JoinTable(
            name = "customer_storekeeper",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "storekeeper_id")
    )
    private List<StorekeeperEntity> storekeepers = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", referencedColumnName = "id", insertable = false, updatable = false)
    private List<AddressEntity> addresses = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "default_store_id")
    private StorekeeperEntity defaultStore;

}
