package com.ecom.Shopping_Cart.Repository;

import com.ecom.Shopping_Cart.Model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByIsActiveTrue();

    List<Product> findByCategory(String category);
    List<Product> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch,String ch2);

    Page<Product> findByIsActiveTrue(Pageable pageable);
    Page<Product> findByCategory(Pageable pageable,String category);


}
