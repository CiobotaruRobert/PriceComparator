package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscountInfoDto {
    private String productId;
    private String productName;
    private String productBrand;
    private LocalDate fromDate;
    private LocalDate toDate;
    private int discountPercentage;
}
