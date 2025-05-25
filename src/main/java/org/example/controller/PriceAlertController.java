package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.model.PriceAlert;
import org.example.model.Product;
import org.example.repository.PriceAlertRepository;
import org.example.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class PriceAlertController {

    private final PriceAlertRepository priceAlertRepository;
    private final ProductRepository productRepository;

    @PostMapping("/api/price-alerts")
    public PriceAlert createAlert(@RequestBody PriceAlert alert) {
        Product product = productRepository.findById(alert.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        alert.setProduct(product);
        alert.setActive(true);

        return priceAlertRepository.save(alert);
    }
}

