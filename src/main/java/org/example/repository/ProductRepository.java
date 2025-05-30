package org.example.repository;

import org.example.model.DiscountEntry;
import org.example.model.Product;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {

    @Query(value = """
    SELECT
        p.id as productId,
        p.name as productName,
        p.brand as productBrand,
        d.store as storeName,
        d.from_date as fromDate,
        d.to_date as toDate,
        d.discount_percentage as discountPercentage,
        pr.price as normalPrice
    FROM product p
    JOIN discount_entry d ON p.id = d.product_id
    JOIN price_entry pr ON pr.product_id = p.id AND pr.store = d.store
        AND pr.date = (
            SELECT MAX(pr2.date)
            FROM price_entry pr2
            WHERE pr2.product_id = p.id
              AND pr2.store = d.store
              AND pr2.date <= :date
        )
    WHERE :date BETWEEN d.from_date AND d.to_date
      AND d.discount_percentage = (
          SELECT MAX(d2.discount_percentage)
          FROM discount_entry d2
          WHERE d2.product_id = p.id
            AND :date BETWEEN d2.from_date AND d2.to_date
      )
    ORDER BY d.discount_percentage DESC
    LIMIT :limit
    """, nativeQuery = true)
    List<Object[]> findProductsWithHighestDiscountDetails(@Param("date") LocalDate date, @Param("limit") int limit);

    List<Product> findByName(String name);
}
