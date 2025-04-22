package com.ecom.Shopping_Cart.Repository;

import com.ecom.Shopping_Cart.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByIsActiveTrue();

    List<Product> findByCategory(String category);
}
