    package com.code.monks.nukkad.entities;
    
    import jakarta.persistence.*;
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    
    @Entity(name = "user_device_token")
    @Data
    @EqualsAndHashCode(callSuper = true)
    public class UserDeviceTokenEntity extends BaseEntity{
    
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;
    
        @Column(name = "device_token")
        private String deviceToken;
    
        @Column(name = "customer_id")
        private Long customerId;
    
        @Column(name = "storekeeper_id")
        private Long storeKeeperId;
    
    }
