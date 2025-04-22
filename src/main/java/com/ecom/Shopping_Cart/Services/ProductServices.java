package com.ecom.Shopping_Cart.Services;

import com.ecom.Shopping_Cart.Model.Product;

import java.util.List;

public interface ProductServices {

    Product saveProduct(Product product);
    List<Product> getAllProducts();
    Boolean deleteProduct(int id);

    Product getProduct(int id);

    List<Product> getAllActiveProducts(String category);

}
