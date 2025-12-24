package com.code.monks.nukkad.entities;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "exception_log")
@EqualsAndHashCode(callSuper = true)
public class ExceptionLogEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message")
    private String message;

    @Column(name = "request_path")
    private String requestPath;
}