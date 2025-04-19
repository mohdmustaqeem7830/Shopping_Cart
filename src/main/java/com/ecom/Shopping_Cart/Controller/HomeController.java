package com.ecom.Shopping_Cart.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HomeController {
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
    public String product(){
        return "product";
    }
    @GetMapping("/view_product")
    public String viewproduct(){
        return "view_product";
    }
}
