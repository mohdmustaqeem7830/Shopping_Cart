package com.ecom.Shopping_Cart.Services;

import com.ecom.Shopping_Cart.Model.OrderRequest;

public interface OrderServices {
    public void  saveOrder(Integer userId, OrderRequest orderRequest);
}
