package org.example.service;

import lombok.AllArgsConstructor;
import org.example.model.DiscountEntry;
import org.example.model.PriceEntry;
import org.example.model.Product;
import org.example.dto.ProductSubstituteDto;
import org.example.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
        LocalDate today = LocalDate.now();

        Map<String, Optional<PriceEntry>> latestActiveEntriesByStore = similar.getPriceEntries().stream()
                .filter(pe -> !pe.getDate().isAfter(today))
                .collect(Collectors.groupingBy(
                        PriceEntry::getStore,
                        Collectors.maxBy(Comparator.comparing(PriceEntry::getDate))
                ));

        Map<String, Optional<Double>> activeDiscountsByStore = similar.getDiscountEntries().stream()
                .filter(de -> !de.getFromDate().isAfter(today) && !de.getToDate().isBefore(today))
                .collect(Collectors.groupingBy(
                        de -> de.getStore().toLowerCase().trim(),
                        Collectors.mapping(
                                de -> de.getDiscountPercentage().doubleValue(),
                                Collectors.maxBy(Double::compareTo)
                        )
                ));

        return latestActiveEntriesByStore.values().stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(pe -> {
                    double quantityInBase = convertToBaseUnit(similar.getQuantity(), similar.getUnit());
                    double originalPrice = pe.getPrice();

                    String storeKey = pe.getStore().toLowerCase().trim();

                    double discountPercent = activeDiscountsByStore.getOrDefault(storeKey, Optional.empty())
                            .orElse(0.0);

                    double discountedPrice = originalPrice * (1 - discountPercent / 100.0);

                    double pricePerUnit = discountedPrice / quantityInBase;

                    return new ProductSubstituteDto(
                            similar.getId(),
                            similar.getName(),
                            similar.getBrand(),
                            pe.getStore(),
                            discountedPrice,
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