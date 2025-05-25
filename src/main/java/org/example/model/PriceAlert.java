package org.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PriceAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;
    private double targetPrice;
    private boolean active = true;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}

