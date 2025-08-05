package com.code.monks.nukkad.entities;

import com.code.monks.nukkad.enums.NotificationStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "Notification_status")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationStatusEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "status")
    private NotificationStatusEnum status;
}
