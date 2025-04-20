package com.ecom.Shopping_Cart.Service.Impl;

import com.ecom.Shopping_Cart.Model.Product;
import com.ecom.Shopping_Cart.Repository.ProductRepository;
import com.ecom.Shopping_Cart.Services.ProductServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductServices {
    @Autowired
    private ProductRepository productRepository;
    @Override
    public Product saveProduct(Product product) {
       return  productRepository.save(product);
    }
}
