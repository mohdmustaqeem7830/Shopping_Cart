package com.ecom.Shopping_Cart.Service.Impl;
import com.ecom.Shopping_Cart.Model.Cart;
import com.ecom.Shopping_Cart.Model.OrderAddress;
import com.ecom.Shopping_Cart.Model.OrderRequest;
import com.ecom.Shopping_Cart.Model.ProductOrder;
import com.ecom.Shopping_Cart.Repository.CartRepository;
import com.ecom.Shopping_Cart.Repository.ProductOrderRepository;
import com.ecom.Shopping_Cart.Services.OrderServices;
import com.ecom.Shopping_Cart.Utils.CommonUtils;
import com.ecom.Shopping_Cart.Utils.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServicesImpl implements OrderServices {

    @Autowired
    ProductOrderRepository productOrderRepository;
    @Autowired
    CartRepository cartRepository;

    @Autowired
    private CommonUtils commonUtils;


    @Override
    public void saveOrder(Integer userId, OrderRequest orderRequest) {
        List<Cart> carts = cartRepository.findByUserId(userId);
        for (Cart cart : carts) {
            ProductOrder productOrder = new ProductOrder();
            productOrder.setOrderId(UUID.randomUUID().toString());
            productOrder.setOrderDate(LocalDate.now());
            productOrder.setProduct(cart.getProduct());
            productOrder.setPrice(cart.getProduct().getDiscountPrice());
            productOrder.setQuantity(cart.getQuantity());
            productOrder.setUser(cart.getUser());
            productOrder.setStatus(OrderStatus.IN_PROGRESS.getName());
            productOrder.setPaymentType(orderRequest.getPaymentType());

            OrderAddress orderAddress = new OrderAddress();
            orderAddress.setFirstName(orderRequest.getFirstName());
            orderAddress.setLastName(orderRequest.getLastName());
            orderAddress.setEmail(orderRequest.getEmail());
            orderAddress.setMobileNo(orderRequest.getMobileNo());
            orderAddress.setAddress(orderRequest.getAddress());
            orderAddress.setCity(orderRequest.getCity());
            orderAddress.setState(orderRequest.getState());
            orderAddress.setPincode(orderRequest.getPincode());

            productOrder.setOrderAddress(orderAddress);

            productOrderRepository.save(productOrder);

        }
    }

    @Override
    public List<ProductOrder> getOrdersByUser(Integer userId) {
        List<ProductOrder> orders = productOrderRepository.findByUserId(userId);
        return orders;
    }

    @Override
    public ProductOrder updateOrderStatus(Integer orderId, String st) {
         ProductOrder order = productOrderRepository.findById(orderId).get();
         if (ObjectUtils.isEmpty(order)){
             return null;
         }else{
                order.setStatus(st);
               ProductOrder updateOrder =  productOrderRepository.save(order);
               return updateOrder;
         }
    }

    @Override
    public List<ProductOrder> getAllOrders() {
        return productOrderRepository.findAll();
    }


    @Override
    public Page<ProductOrder> getAllOrdersPagination(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        return productOrderRepository.findAll(pageable);
    }


    @Override
    public ProductOrder getOrderById(String orderId) {
        return productOrderRepository.findByOrderId(orderId);
    }


}
