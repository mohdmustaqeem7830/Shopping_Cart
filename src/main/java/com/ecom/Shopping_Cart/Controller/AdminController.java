package com.ecom.Shopping_Cart.Controller;


import com.ecom.Shopping_Cart.Model.Category;
import com.ecom.Shopping_Cart.Services.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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
    public String saveCategory(@ModelAttribute Category category, @RequestParam("file")MultipartFile file, HttpSession session) throws IOException {

        String filename = file!=null ? file.getOriginalFilename() : "default.jpg";
        category.setImageName(filename);

        if (categoryService.existCategory(category.getName())){
            session.setAttribute("errorMsg","Category already exists");
        }else{
           Category saveCategory =  categoryService.saveCategory(category);
            if (ObjectUtils.isEmpty(saveCategory)){
                session.setAttribute("errorMsg","Not Saved ! Internal Server error");
            }else{
                session.setAttribute(" succMsg","Category Saved successfully");
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+"category_img"+File.separator+file.getOriginalFilename());

                System.out.println(path);
                Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        return "redirect:/admin/category";
    }
}
