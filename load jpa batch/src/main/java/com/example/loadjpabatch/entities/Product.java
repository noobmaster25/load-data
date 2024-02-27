package com.example.loadjpabatch.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name="product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idproduct")
    private Long productId;
    private String band;
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(productId, product.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }
}
