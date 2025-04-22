package com.ecom.Shopping_Cart.Repository;

import com.ecom.Shopping_Cart.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {

    public Boolean existsByName(String name);

    List<Category> findByIsActiveTrue();
}
