package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.dto.PriceHistoryPointDto;
import org.example.service.PriceHistoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class PriceHistoryController {

    private final PriceHistoryService priceHistoryService;

    @GetMapping("/api/price-history")
    public List<PriceHistoryPointDto> getPriceHistory(
            @RequestParam String productId,
            @RequestParam(required = false) String store,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand
    ) {
        return priceHistoryService.getPriceHistory(productId, store, category, brand);
    }
}
