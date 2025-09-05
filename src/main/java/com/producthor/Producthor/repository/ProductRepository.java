package com.producthor.Producthor.repository;

import com.producthor.Producthor.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryId(Long categoryId);

    List<Product> findByAvailableTrue();

    List<Product> findByCategoryIdAndAvailableTrue(Long categoryId);

    @Modifying
    @Query("update Product p set p.category = null where p.category.id = :categoryId")
    void clearCategoryFromProducts(Long categoryId);
}