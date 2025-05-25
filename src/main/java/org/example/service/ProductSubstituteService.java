package org.example.service;

import lombok.AllArgsConstructor;
import org.example.model.Product;
import org.example.dto.ProductSubstituteDto;
import org.example.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductSubstituteService {

    private final ProductRepository productRepository;

    public List<ProductSubstituteDto> getSubstituteProducts(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        String productName = product.getName();

        List<Product> similarProducts = productRepository.findByName(productName).stream()
                .filter(p -> !p.getId().equals(productId))
                .collect(Collectors.toList());

        return similarProducts.stream()
                .flatMap(this::mapProductToSubstitutes)
                .sorted(Comparator.comparingDouble(ProductSubstituteDto::getPricePerUnit))
                .collect(Collectors.toList());
    }

    private Stream<ProductSubstituteDto> mapProductToSubstitutes(Product similar) {
        return similar.getPriceEntries().stream()
                .map(pe -> {
                    double quantityInBase = convertToBaseUnit(similar.getQuantity(), similar.getUnit());
                    double pricePerUnit = pe.getPrice() / quantityInBase;
                    return new ProductSubstituteDto(
                            similar.getId(),
                            similar.getName(),
                            similar.getBrand(),
                            pe.getPrice(),
                            similar.getQuantity(),
                            similar.getUnit(),
                            pricePerUnit
                    );
                });
    }

    private double convertToBaseUnit(double quantity, String unit) {
        return switch (unit.toLowerCase()) {
            case "g" -> quantity / 1000.0;
            case "kg" -> quantity;
            case "ml" -> quantity / 1000.0;
            case "l" -> quantity;
            default -> quantity;
        };
    }
}
