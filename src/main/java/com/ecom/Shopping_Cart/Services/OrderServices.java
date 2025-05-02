package com.ecom.Shopping_Cart.Services;

import com.ecom.Shopping_Cart.Model.OrderRequest;
import com.ecom.Shopping_Cart.Model.ProductOrder;
import com.ecom.Shopping_Cart.Utils.OrderStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderServices {
    public void  saveOrder(Integer userId, OrderRequest orderRequest);

    public List<ProductOrder>  getOrdersByUser(Integer userId);

    ProductOrder updateOrderStatus(Integer orderId,String st);

    List<ProductOrder>  getAllOrders();
    ProductOrder  getOrderById(String orderId);

    Page<ProductOrder> getAllOrdersPagination(Integer pageNo,Integer pageSize);

}
