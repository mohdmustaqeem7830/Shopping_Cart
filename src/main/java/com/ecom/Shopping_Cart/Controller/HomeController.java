package com.ecom.Shopping_Cart.Controller;

import com.ecom.Shopping_Cart.Model.Category;
import com.ecom.Shopping_Cart.Model.Product;
import com.ecom.Shopping_Cart.Services.CategoryService;
import com.ecom.Shopping_Cart.Services.ProductServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductServices productServices;

     @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }


    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @GetMapping("/product")
    public String product(Model m){
        List<Category>  categories = categoryService.getAllActiveCategory();
        List<Product> products = productServices.getAllActiveProducts();
        m.addAttribute("categories", categories);
        m.addAttribute("products", products);

        return "product";
    }
    @GetMapping("/view_product")
    public String viewproduct(){
        return "view_product";
    }
}
