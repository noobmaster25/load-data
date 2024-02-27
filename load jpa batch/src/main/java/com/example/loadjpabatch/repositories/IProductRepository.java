package com.example.loadjpabatch.repositories;

import com.example.loadjpabatch.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<Product,Long> {
    @Query("SELECT p.productId FROM Product as p WHERE p.productId IN :ids")
    List<Long> findByProductIdIn(List<Long> ids);
}
