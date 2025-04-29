package com.ecom.Shopping_Cart.Repository;

import com.ecom.Shopping_Cart.Model.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Integer> {
}
