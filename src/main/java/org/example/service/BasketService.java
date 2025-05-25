package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.BasketItemDto;
import org.example.dto.OptimizedItemDto;
import org.example.model.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BasketService {
    private final ProductService productService;

    public Map<String, List<OptimizedItemDto>> optimizeBasket(List<BasketItemDto> basket, LocalDate date) {

        Map<String, List<OptimizedItemDto>> storeToItems = new HashMap<>();

        for (BasketItemDto item : basket) {
            Product product = productService.getProductById(item.getProductId());
            if (product == null) continue;

            PriceEntry bestEntry = null;
            int bestDiscount = 0;
            double bestFinalPrice = Double.MAX_VALUE;

            Map<String, PriceEntry> latestEntries = new HashMap<>();
            for (PriceEntry pe : product.getPriceEntries()) {
                if (pe.getDate().isAfter(date)) continue;

                PriceEntry currentLatest = latestEntries.get(pe.getStore());
                if (currentLatest == null || pe.getDate().isAfter(currentLatest.getDate())) {
                    latestEntries.put(pe.getStore(), pe);
                }
            }

            for (PriceEntry pe : latestEntries.values()) {
                int discount = product.getDiscountEntries().stream()
                        .filter(de -> de.getStore().equals(pe.getStore())
                                && !date.isBefore(de.getFromDate())
                                && !date.isAfter(de.getToDate()))
                        .mapToInt(DiscountEntry::getDiscountPercentage)
                        .findFirst().orElse(0);

                double finalPrice = pe.getPrice() * (1 - discount / 100.0);

                if (finalPrice < bestFinalPrice) {
                    bestEntry = pe;
                    bestDiscount = discount;
                    bestFinalPrice = finalPrice;
                }
            }

            if (bestEntry != null) {
                OptimizedItemDto oi = new OptimizedItemDto(
                        product.getId(),
                        product.getName(),
                        item.getQuantity(),
                        bestFinalPrice,
                        bestDiscount
                );

                storeToItems.computeIfAbsent(bestEntry.getStore(), k -> new ArrayList<>()).add(oi);
            }
        }
        return storeToItems;
    }
}
