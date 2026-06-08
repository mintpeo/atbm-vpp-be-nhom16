package com.atbm.appvppbe.dto.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "signature")
public class Signature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order orderId;

    @Column(name = "signature")
    private String signature;

    @Column(name = "public_key")
    private String publicKey;
}
