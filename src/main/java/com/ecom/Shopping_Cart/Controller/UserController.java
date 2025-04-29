package com.ecom.Shopping_Cart.Controller;

import com.ecom.Shopping_Cart.Model.*;
import com.ecom.Shopping_Cart.Services.CartService;
import com.ecom.Shopping_Cart.Services.CategoryService;
import com.ecom.Shopping_Cart.Services.OrderServices;
import com.ecom.Shopping_Cart.Services.UserService;
import com.ecom.Shopping_Cart.Utils.OrderStatus;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private OrderServices orderServices;


    @ModelAttribute
    public  void getUserDetail(Principal p, Model m){

        if (p!=null){
            String email = p.getName();
            UserDtls user = userService.getUserByEmail(email);
            m.addAttribute("user", user);
            Integer countCart = cartService.getCountCart(user.getId());
            m.addAttribute("countCart", countCart);
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

    @GetMapping("/cart")
    public String loadCartPage(Principal p , Model m){
        UserDtls user = getLoggedInUserDetail(p);
        List<Cart> carts =  cartService.getCartByUserId(user.getId());
        m.addAttribute("carts",carts);

        if (carts.size()>0){
            Double totalOrderPrice  = carts.get(carts.size()-1).getTotalOrderPrice();
            m.addAttribute("totalOrderPrice",totalOrderPrice);
        }

        return "/user/cart";
    }


    public UserDtls getLoggedInUserDetail(Principal p){
        UserDtls user = userService.getUserByEmail(p.getName());
        return user;
    }

    @GetMapping("/cartQuantityUpdate")
    public String updateCartQuantity(@RequestParam String sy,@RequestParam Integer cid, HttpSession session) {
        cartService.updateCartQuantity(sy,cid);
        return "redirect:/user/cart";
    }


//    Orders code start from here

    @GetMapping("/orders")
    public String orderPage(Principal p, Model m) {

        UserDtls user = getLoggedInUserDetail(p);
        List<Cart> carts =  cartService.getCartByUserId(user.getId());
        m.addAttribute("carts",carts);

        if (carts.size()>0){

            Double orderPrice  = carts.get(carts.size()-1).getTotalOrderPrice();
//            total price with delivery fee and the tax amount included
            Double totalOrderPrice  = carts.get(carts.size()-1).getTotalOrderPrice()+250+100;
            m.addAttribute("orderPrice",orderPrice);
            m.addAttribute("totalOrderPrice",totalOrderPrice);
        }

        return "user/order";
    }

    @PostMapping("/saveOrder")
    public String saveOrder(@ModelAttribute OrderRequest orderRequest, Principal p, HttpSession session) {
       UserDtls user = getLoggedInUserDetail(p);
        orderServices.saveOrder(user.getId(),orderRequest);
        return "redirect:/user/success";
    }


    @GetMapping("/success")
    public String loadSuccess() {
        return "/user/success";
    }


    @GetMapping("/userOrders")
    public String myOrder(Principal p , Model m) {
        UserDtls user = getLoggedInUserDetail(p);
        List<ProductOrder>  orders = orderServices.getOrdersByUser(user.getId());
        m.addAttribute("orders",orders);
        return "/user/myOrder";
    }

    @GetMapping("/updateStatus")
    public String updateStatus(@RequestParam Integer id, @RequestParam Integer st,HttpSession session){
        OrderStatus[]   values = OrderStatus.values();
        String status = null;

        for (OrderStatus os : values) {
            if (os.getId().equals(st))
            {
                status = os.getName();
            }

        }

        Boolean updateOrder =  orderServices.updateOrderStatus(id,status);
        if (updateOrder){
            session.setAttribute("succMsg", "Status updated");
        }else{
            session.setAttribute("errorMsg", "Something went wrong on server");
        }
        return "redirect:/user/userOrders";
    }



}
