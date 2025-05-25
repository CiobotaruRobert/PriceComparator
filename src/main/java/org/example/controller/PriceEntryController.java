package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.model.PriceEntry;
import org.example.model.Product;
import org.example.repository.ProductRepository;
import org.example.service.PriceAlertService;
import org.example.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@AllArgsConstructor
public class PriceEntryController {

    private final ProductRepository productRepository;
    private final ProductService productService;
    private final PriceAlertService priceAlertService;

    @PostMapping("/api/products/{productId}/add-price-entry")
    public void addPriceEntryManually(
            @PathVariable String productId,
            @RequestBody Map<String, Object> body) {

        String store = (String) body.get("store");
        String date = (String) body.get("date");
        Double price = ((Number) body.get("price")).doubleValue();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        PriceEntry priceEntry = new PriceEntry(store, LocalDate.parse(date), Double.valueOf(price));
        product.addPriceEntry(priceEntry);

        productService.addOrUpdateProduct(product);
        priceAlertService.checkAlerts(product, price);
    }
}

