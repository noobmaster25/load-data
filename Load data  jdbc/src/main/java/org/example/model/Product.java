package org.example.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private Long productId;
    private String brand;

    private BigDecimal price;

    private Long categoryId;


    public Product() {

    }

    public Product(Long productId, String brand, BigDecimal price, Long category_id) {
        this.productId = productId;
        this.brand = brand;
        this.price = price;
        this.categoryId = category_id;
    }


    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return productId.equals(product.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", brand='" + brand + '\'' +
                ", price=" + price +
                ", categoryId=" + categoryId +
                '}';
    }
}
