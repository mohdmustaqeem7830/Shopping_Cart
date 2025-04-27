package com.ecom.Shopping_Cart.Controller;

import com.ecom.Shopping_Cart.Model.Cart;
import com.ecom.Shopping_Cart.Model.Category;
import com.ecom.Shopping_Cart.Model.UserDtls;
import com.ecom.Shopping_Cart.Services.CartService;
import com.ecom.Shopping_Cart.Services.CategoryService;
import com.ecom.Shopping_Cart.Services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CartService cartService;


    @ModelAttribute
    public  void getUserDetail(Principal p, Model m){

        if (p!=null){
            String email = p.getName();
            UserDtls user = userService.getUserByEmail(email);
            m.addAttribute("user", user);
        }

        List<Category> allActiveCategory = categoryService.getAllActiveCategory();
        m.addAttribute("categories", allActiveCategory);
    }


    @GetMapping("/")
    public String index() {
        return "user/home";
    }


    //    cart code start from here
    @GetMapping("/addCart")
    public String addToCart(@RequestParam Integer pid, @RequestParam Integer uid, HttpSession session) {

        Cart saveCart = cartService.saveCart(pid, uid);

        if (ObjectUtils.isEmpty(saveCart)) {
            session.setAttribute("errorMsg", "Product add to cart failed");
        } else {
            session.setAttribute("succMsg", "Product added to cart");
        }
        return "redirect:/view_product/" + pid;}



}
