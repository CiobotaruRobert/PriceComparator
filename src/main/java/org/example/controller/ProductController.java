package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.Product;
import org.example.repository.ProductRepository;
import org.example.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    ProductRepository repository;

    private final ProductService productService;

    @GetMapping("/all")
    public Collection<Product> getAllProducts() {
        return productService.getAllProducts();
    }
}
