package com.code.monks.nukkad.entities;

import com.code.monks.nukkad.enums.NotificationStatusEnum;
import jakarta.persistence.*;
import lombok.Data;

@Entity(name = "user_device_token")
@Data
public class UserDeviceTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "device_token")
    private String deviceToken;

    @Column(name = "notification_status")
    private NotificationStatusEnum notificationStatus;

    @OneToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private CustomerEntity customer;

    @OneToOne
    @JoinColumn(name = "storekeeper_id", referencedColumnName = "id")
    private StorekeeperEntity storeKeeper;
}
