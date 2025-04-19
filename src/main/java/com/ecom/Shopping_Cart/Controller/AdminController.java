package com.ecom.Shopping_Cart.Controller;


import com.ecom.Shopping_Cart.Model.Category;
import com.ecom.Shopping_Cart.Services.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CategoryService categoryService;
    @GetMapping("/")
    public String index(){
        return "admin/index";
    }


    @GetMapping("/loadAddProduct")
    public String loadAddProduct(){
        return "admin/add_product";
    }


    @GetMapping("/category")
    public String category(){
        return "admin/category";
    }

@PostMapping("/saveCategory")
    public String saveCategory(@ModelAttribute Category category, HttpSession session){

        if (categoryService.existCategory(category.getName())){
            session.setAttribute("error msg: ","Category already exists");
        }else{
           Category saveCategory =  categoryService.saveCategory(category);
            if (ObjectUtils.isEmpty(saveCategory)){
                session.setAttribute("error msg: ","Not Saved ! Internal Server error");
            }else{
                session.setAttribute("error msg: ","Category Saved successfully");
            }
        }
        return "redirct:/category";
    }
}
