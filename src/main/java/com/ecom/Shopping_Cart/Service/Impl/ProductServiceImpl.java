package com.ecom.Shopping_Cart.Service.Impl;

import com.ecom.Shopping_Cart.Model.Product;
import com.ecom.Shopping_Cart.Repository.ProductRepository;
import com.ecom.Shopping_Cart.Services.ProductServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductServices {
    @Autowired
    private ProductRepository productRepository;
    @Override
    public Product saveProduct(Product product) {
       return  productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Boolean deleteProduct(int id) {
     if(productRepository.existsById(id)) {
         productRepository.deleteById(id);
         return true;
     }else{
         return false;
     }
    }

    @Override
    public Product getProduct(int id) {
      return  productRepository.findById(id).orElse(null);
    }

    @Override
    public List<Product> getAllActiveProducts(String category) {
        List<Product> products;
        if (ObjectUtils.isEmpty(category)){
            products = productRepository.findByIsActiveTrue();
        }else{
            products = productRepository.findByCategory(category);
        }
        return products;
    }

    @Override
    public List<Product> searchProducts(String ch) {

        List<Product> products = productRepository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch,ch);
        return products;

    }

    @Override
    public Page<Product> getAllActiveProductsPagination(Integer pagenNo, Integer pageSize,String category) {

        Pageable pageable = PageRequest.of(pagenNo,pageSize);
        Page<Product> pageProduct = null;

        List<Product> products;
        if (ObjectUtils.isEmpty(category)){
            pageProduct = productRepository.findByIsActiveTrue(pageable);
        }else{
            pageProduct = productRepository.findByCategory(pageable,category);
        }
        return pageProduct;
    }
}
