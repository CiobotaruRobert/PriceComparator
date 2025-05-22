package org.example.repository;

import org.example.model.DiscountEntry;
import org.example.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DiscountEntryRepository extends JpaRepository<DiscountEntry, Long> {

    List<DiscountEntry> findByFromDateIn(List<LocalDate> dates);

    Optional<DiscountEntry> findByProductAndStoreAndFromDate(Product product, String store, LocalDate fromDate);
}
