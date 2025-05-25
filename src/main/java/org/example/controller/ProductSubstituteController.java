package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.ProductSubstituteDto;
import org.example.service.ProductSubstituteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductSubstituteController {

    private final ProductSubstituteService productSubstituteService;

    @GetMapping("/api/product-substitutes")
    public List<ProductSubstituteDto> getProductSubstitutes(@RequestParam String productId) {
        return productSubstituteService.getSubstituteProducts(productId);
    }
}
