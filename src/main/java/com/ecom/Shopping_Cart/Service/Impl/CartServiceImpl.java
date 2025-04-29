package com.ecom.Shopping_Cart.Service.Impl;

import com.ecom.Shopping_Cart.Model.Cart;
import com.ecom.Shopping_Cart.Model.Product;
import com.ecom.Shopping_Cart.Model.UserDtls;
import com.ecom.Shopping_Cart.Repository.CartRepository;
import com.ecom.Shopping_Cart.Repository.ProductRepository;
import com.ecom.Shopping_Cart.Repository.UserRepository;
import com.ecom.Shopping_Cart.Services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Cart saveCart(Integer productId, Integer userId) {
        UserDtls userDtls = userRepository.findById(userId).get();
        Product product = productRepository.findById(productId).get();

        Cart cartStatus = cartRepository.findByProductIdAndUserId(productId,userId);

        Cart cart = null;

        if (ObjectUtils.isEmpty(cartStatus)){
            cart = new Cart();
            cart.setProduct(product);
            cart.setUser(userDtls);
            cart.setQuantity(1);
            cart.setTotalPrice(1*product.getDiscountPrice());
        }else{
            cart = cartStatus;
            cart.setQuantity(cart.getQuantity()+1);
            cart.setTotalPrice(cart.getQuantity()*cart.getProduct().getDiscountPrice());
        }

        Cart saveCart= cartRepository.save(cart);


        return saveCart;
    }

    @Override
    public List<Cart> getCartByUserId(Integer userId) {
       List<Cart> carts =  cartRepository.findByUserId(userId);
       Double totalOrderPrice = 0.0;

       List<Cart> updateCarts  = new ArrayList<>();
       for (Cart cart : carts) {

           Double totalPrice = (cart.getProduct().getDiscountPrice()*cart.getQuantity());
           cart.setTotalPrice(totalPrice);
           totalOrderPrice = totalOrderPrice+totalPrice;
           cart.setTotalOrderPrice(totalOrderPrice);
           updateCarts.add(cart);

       }
       return updateCarts;
    }

    @Override
    public Integer getCountCart(Integer userId) {

        Integer countByUserId = cartRepository.countByUserId(userId);
        return countByUserId;
    }

    @Override
    public void updateCartQuantity(String sy, Integer cartId) {

        Cart cart = cartRepository.findById(cartId).get();
        int updateQuantity;
        if (sy.equalsIgnoreCase("de")){
           updateQuantity = cart.getQuantity()-1;
           if (updateQuantity<=0){
               cartRepository.delete(cart);
           }else{
               cart.setQuantity(updateQuantity);
               cartRepository.save(cart);
           }
        }else{
            updateQuantity = cart.getQuantity()+1;
            cart.setQuantity(updateQuantity);
            cartRepository.save(cart);
        }

    }
}
