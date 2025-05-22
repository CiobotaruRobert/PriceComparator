package org.example.service;

import lombok.AllArgsConstructor;
import org.example.model.DiscountEntry;
import org.example.dto.DiscountInfoDto;
import org.example.repository.DiscountEntryRepository;
import org.example.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DiscountService {

    private final ProductRepository productRepository;
    private final DiscountEntryRepository discountEntryRepository;

    public List<DiscountInfoDto> getBestDiscounts(LocalDate date, int limit) {
        List<Object[]> results = productRepository.findProductsWithHighestDiscountDetails(date, limit);

        return results.stream()
                .map(row -> new DiscountInfoDto(
                        (String) row[0],
                        (String) row[1],
                        (String) row[2],
                        ((java.sql.Date) row[3]).toLocalDate(),
                        ((java.sql.Date) row[4]).toLocalDate(),
                        ((Number) row[5]).intValue()
                ))
                .collect(Collectors.toList());
    }

    public List<DiscountInfoDto> getNewDiscounts(LocalDate today) {
        LocalDate yesterday = today.minusDays(1);
        List<DiscountEntry> recentDiscounts = discountEntryRepository.findByFromDateIn(List.of(today, yesterday));

        return recentDiscounts.stream()
                .map(d -> new DiscountInfoDto(
                        d.getProduct().getId(),
                        d.getProduct().getName(),
                        d.getProduct().getBrand(),
                        d.getFromDate(),
                        d.getToDate(),
                        d.getDiscountPercentage()
                ))
                .collect(Collectors.toList());
    }

}


