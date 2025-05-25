package org.example.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSubstituteDto {
    private String productId;
    private String name;
    private String brand;
    private String store;
    private double price;
    private double quantity;
    private String unit;
    private double pricePerUnit;
}