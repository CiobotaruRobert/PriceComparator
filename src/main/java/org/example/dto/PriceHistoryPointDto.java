package org.example.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PriceHistoryPointDto {
    private LocalDate date;
    private double price;
}