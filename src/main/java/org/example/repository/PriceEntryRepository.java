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
    Optional<PriceEntry> findByProductAndDateAndStore(Product product, LocalDate date, String store);
}
