package org.example.service;

import lombok.AllArgsConstructor;
import org.example.model.DiscountEntry;
import org.example.dto.DiscountInfoDto;
import org.example.model.PriceEntry;
import org.example.model.Product;
import org.example.repository.DiscountEntryRepository;
import org.example.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DiscountService {

    private final ProductRepository productRepository;
    private final DiscountEntryRepository discountEntryRepository;

    public List<DiscountInfoDto> getBestDiscounts(LocalDate date, int limit) {
        List<Object[]> results = productRepository.findProductsWithHighestDiscountDetails(date, limit);

        return results.stream()
                .map(row -> {
                    String productId = (String) row[0];
                    String productName = (String) row[1];
                    String productBrand = (String) row[2];
                    String store = (String) row[3];
                    LocalDate fromDate = ((java.sql.Date) row[4]).toLocalDate();
                    LocalDate toDate = ((java.sql.Date) row[5]).toLocalDate();
                    int discountPercentage = ((Number) row[6]).intValue();
                    double normalPrice = ((Number) row[7]).doubleValue();

                    double discountedPrice = BigDecimal.valueOf(
                            normalPrice * (1 - discountPercentage / 100.0)
                    ).setScale(2, RoundingMode.HALF_UP).doubleValue();

                    return new DiscountInfoDto(
                            productId,
                            productName,
                            productBrand,
                            store,
                            fromDate,
                            toDate,
                            discountPercentage,
                            normalPrice,
                            discountedPrice
                    );
                })
                .collect(Collectors.toList());
    }

    public List<DiscountInfoDto> getNewDiscounts(LocalDate today) {
        LocalDate yesterday = today.minusDays(1);
        List<DiscountEntry> recentDiscounts = discountEntryRepository.findByFromDateIn(List.of(today, yesterday));

        return recentDiscounts.stream()
                .map(d -> {
                    Product product = d.getProduct();
                    String store = d.getStore().trim().toLowerCase();

                    Optional<PriceEntry> priceEntryOpt = product.getPriceEntries().stream()
                            .filter(pe ->
                                    pe.getStore().trim().equalsIgnoreCase(store) &&
                                            !pe.getDate().isAfter(today)
                            )
                            .max(Comparator.comparing(PriceEntry::getDate));

                    double normalPrice = priceEntryOpt.map(PriceEntry::getPrice).orElse(0.0);
                    double discountedPrice = BigDecimal.valueOf(normalPrice * (1 - d.getDiscountPercentage() / 100.0))
                            .setScale(2, RoundingMode.HALF_UP)
                            .doubleValue();

                    return new DiscountInfoDto(
                            product.getId(),
                            product.getName(),
                            product.getBrand(),
                            d.getStore(),
                            d.getFromDate(),
                            d.getToDate(),
                            d.getDiscountPercentage(),
                            normalPrice,
                            discountedPrice
                    );
                })
                .collect(Collectors.toList());
    }

}


