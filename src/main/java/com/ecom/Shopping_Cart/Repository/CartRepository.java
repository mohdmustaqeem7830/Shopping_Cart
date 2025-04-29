package com.ecom.Shopping_Cart.Repository;
import com.ecom.Shopping_Cart.Model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    public Cart findByProductIdAndUserId(Integer productId, Integer userId);

    Integer countByUserId(Integer userId);

    List<Cart> findByUserId(Integer userId);
}
