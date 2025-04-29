package com.ecom.Shopping_Cart.Services;

import com.ecom.Shopping_Cart.Model.OrderRequest;
import com.ecom.Shopping_Cart.Model.ProductOrder;
import com.ecom.Shopping_Cart.Utils.OrderStatus;

import java.util.List;

public interface OrderServices {
    public void  saveOrder(Integer userId, OrderRequest orderRequest);

    public List<ProductOrder>  getOrdersByUser(Integer userId);

    Boolean updateOrderStatus(Integer orderId,String st);
}
