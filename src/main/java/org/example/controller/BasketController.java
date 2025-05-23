package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.BasketItemDto;
import org.example.dto.OptimizedItemDto;
import org.example.service.BasketService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/basket")
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;

    @PostMapping("/optimize")
    public Map<String, List<OptimizedItemDto>> optimizeBasket(@RequestBody List<BasketItemDto> basket) {
        LocalDate today = LocalDate.now();
        return basketService.optimizeBasket(basket, today);
    }
}