package org.example.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OptimizedItemDto {
    private String productId;
    private String productName;
    private double quantity;
    private double finalPrice;
    private int discountPercentage;
}
