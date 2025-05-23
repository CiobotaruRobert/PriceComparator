package org.example.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BasketItemDto {
    private String productId;
    private double quantity;
}