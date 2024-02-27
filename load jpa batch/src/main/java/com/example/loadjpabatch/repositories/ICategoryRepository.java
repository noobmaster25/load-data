package com.example.loadjpabatch.repositories;

import com.example.loadjpabatch.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ICategoryRepository extends JpaRepository<Category,Long> {
    @Query("SELECT c.categoryId FROM Category as c WHERE c.categoryId IN :ids")
    List<Long> findByCategoryIdIn(List<Long> ids);
}
