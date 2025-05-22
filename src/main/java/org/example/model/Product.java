package org.example.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Product {

    @Id
    @NonNull
    private String id;

    @NonNull
    private String name;

    @NonNull
    private String category;

    @NonNull
    private String brand;

    @NonNull
    private double quantity;

    @NonNull
    private String unit;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<PriceEntry> priceEntries = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<DiscountEntry> discountEntries = new ArrayList<>();

    public void addPriceEntry(PriceEntry entry) {
        this.priceEntries.add(entry);
    }

    public void addDiscountEntry(DiscountEntry entry) {
        this.discountEntries.add(entry);
    }
}