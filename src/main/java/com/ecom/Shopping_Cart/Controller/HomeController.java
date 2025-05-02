package com.ecom.Shopping_Cart.Controller;

import com.ecom.Shopping_Cart.Model.Category;
import com.ecom.Shopping_Cart.Model.Product;
import com.ecom.Shopping_Cart.Model.UserDtls;
import com.ecom.Shopping_Cart.Services.CartService;
import com.ecom.Shopping_Cart.Services.CategoryService;
import com.ecom.Shopping_Cart.Services.ProductServices;
import com.ecom.Shopping_Cart.Services.UserService;
import com.ecom.Shopping_Cart.Utils.CommonUtils;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
public class HomeController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductServices productServices;
    @Autowired
    private UserService userService;
    @Autowired
    private  CommonUtils commonUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CartService cartService;

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
    public String index(){
        return "index";
    }

    @GetMapping("/signin")
    public String login(){
        return "login";
    }


    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @GetMapping("/products")
    public String product(Model m, @RequestParam(value = "category",defaultValue = "") String category,@RequestParam(name = "pageNo",defaultValue = "0")Integer pageNo,
                          @RequestParam(name = "pageSize",defaultValue = "9")Integer pageSize){
        List<Category>  categories = categoryService.getAllActiveCategory();
        m.addAttribute("paramValue", category);
        m.addAttribute("categories", categories);
//        List<Product> products = productServices.getAllActiveProducts(category);
//        m.addAttribute("products", products);

        Page<Product> page = productServices.getAllActiveProductsPagination(pageNo,pageSize,category);
        List<Product> products = page.getContent();
        m.addAttribute("products", products);
        m.addAttribute("productsSize", products.size());
        m.addAttribute("totalPages", page.getTotalPages());
        m.addAttribute("pageNo", page.getNumber());
        m.addAttribute("pageSize", pageSize);
        m.addAttribute("totalElements", page.getTotalElements());
        m.addAttribute("isFirst", page.isFirst());
        m.addAttribute("isLast", page.isLast());




        return "product";
    }
    @GetMapping("/view_product/{id}")
    public String viewproduct(@PathVariable int id, Model m){
         m.addAttribute("product", productServices.getProduct(id));
        return "view_product";
    }


//    code for the user start here

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") UserDtls userDtls, @RequestParam("file" ) MultipartFile file, HttpSession session) throws IOException {

         String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
         userDtls.setProfileImage(imageName);
         UserDtls saveUser =  userService.saveUser(userDtls);

         if (!ObjectUtils.isEmpty(saveUser)){
             if (!file.isEmpty()){
                 File saveFile = new ClassPathResource("static/img").getFile();
                 Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+"profile_img"+File.separator+file.getOriginalFilename());

                 System.out.println(path);
                 Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
             }
             session.setAttribute("succMsg", "Saved Successfuly");
         }else{
             session.setAttribute("errorMsg", "Something went wrong");
         }


         return  "redirect:/register";
    }


//    forgot password code

    @GetMapping("/forgotPassword")
    public String forgotPassword(){
        return "/forgotPassword";
    }


    @PostMapping("forgotPassword")
    public String forgotProcessPassword(@RequestParam String email, HttpSession session, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        UserDtls user = userService.getUserByEmail(email);
        if (!ObjectUtils.isEmpty(user)){

            String resetToken = UUID.randomUUID().toString();
            userService.updateUserResetToken(email, resetToken);

            String url = CommonUtils.generateUrl(request)+"/resetPassword?token="+resetToken;
            Boolean sendmail = commonUtils.sendMail(email,url);


            if (sendmail){
                session.setAttribute("succMsg","Please check your email ! Password Reset link sent");
            }else{
                session.setAttribute("errorMsg","Something wrong on server ! Email not send");
            }
        }else{
            session.setAttribute("errorMsg", "Invalid Email Id");
        }
        return "redirect:/forgotPassword";
    }
    @GetMapping("/resetPassword")
    public String showresetPassword(@RequestParam String token, Model m){
        UserDtls userDtls = userService.getUserByToken(token);
        if (ObjectUtils.isEmpty(userDtls)){
            m.addAttribute("msg", "Your Link is invalid or Expired");
           return "message";
        }
        m.addAttribute("token", token);
        return "/resetPassword";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam String token,@RequestParam String password,Model m){
        UserDtls userDtls = userService.getUserByToken(token);
        if (ObjectUtils.isEmpty(userDtls)) {
            m.addAttribute("msg", "Your Link is invalid or Expired");
            return "message";
        }else{

            String passwordChecker = passwordEncoder.encode(password);
            userDtls.setPassword(passwordChecker);
            userDtls.setResetToken(null);
            userService.updateUser(userDtls);
            m.addAttribute("msg","Password change successfully");
            return "message";
        }
    }

    //code for the search product

    @GetMapping("/search")
    public String  searchProduct(@RequestParam String ch,Model m){

        List<Product> products  = productServices.searchProducts(ch);
        m.addAttribute("products", products);
        List<Category>  categories = categoryService.getAllActiveCategory();
        m.addAttribute("categories", categories);
        return "product";

    }



}
