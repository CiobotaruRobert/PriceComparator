package org.example.service;

import lombok.AllArgsConstructor;
import org.example.dto.PriceHistoryPointDto;
import org.example.repository.PriceEntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PriceHistoryService {

    private final PriceEntryRepository priceEntryRepository;

    public List<PriceHistoryPointDto> getPriceHistory(
            String productId,
            String store,
            String category,
            String brand
    ) {
        return priceEntryRepository.findPriceEntriesByFilters(productId, store, category, brand)
                .stream()
                .map(pe -> new PriceHistoryPointDto(pe.getDate(), pe.getPrice()))
                .collect(Collectors.toList());
    }
}
