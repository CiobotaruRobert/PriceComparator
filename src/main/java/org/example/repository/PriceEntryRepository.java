package org.example.repository;

import org.example.model.PriceEntry;
import org.example.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PriceEntryRepository extends JpaRepository<PriceEntry, Long> {

    @Query("""
        SELECT pe FROM PriceEntry pe
        WHERE (:store IS NULL OR pe.store = :store)
          AND (:category IS NULL OR pe.product.category = :category)
          AND (:brand IS NULL OR pe.product.brand = :brand)
          AND pe.product.id = :productId
        ORDER BY pe.date ASC
    """)
    List<PriceEntry> findPriceEntriesByFilters(
            @Param("productId") String productId,
            @Param("store") String store,
            @Param("category") String category,
            @Param("brand") String brand
    );

    Optional<PriceEntry> findByProductAndDateAndStore(Product product, LocalDate date, String store);
}
