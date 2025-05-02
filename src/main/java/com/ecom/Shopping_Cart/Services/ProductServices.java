package com.ecom.Shopping_Cart.Services;

import com.ecom.Shopping_Cart.Model.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductServices {

    Product saveProduct(Product product);
    List<Product> getAllProducts();
    Boolean deleteProduct(int id);

    Product getProduct(int id);

    List<Product> getAllActiveProducts(String category);
    List<Product> searchProducts(String ch);
    Page<Product> getAllActiveProductsPagination(Integer pagenNo,Integer pageSize,String category);
    Page<Product> searchProductsPagination(String ch,Integer pageNo,Integer pageSize);
    Page<Product> getAllProductsPagination(Integer pagenNo,Integer pageSize);


    Page<Product> searchActiveProductPagination(Integer pageNo, Integer pageSize,String category,String  ch);
}
