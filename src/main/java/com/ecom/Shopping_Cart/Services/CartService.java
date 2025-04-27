package com.ecom.Shopping_Cart.Services;

import com.ecom.Shopping_Cart.Model.Cart;
import com.ecom.Shopping_Cart.Service.Impl.CartServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CartService {

    public Cart saveCart(Integer productId, Integer userId);
    public List<Cart> getCartByUserId(Integer userId);
}
