package com.ecom.Shopping_Cart.Controller;


import com.ecom.Shopping_Cart.Model.Category;
import com.ecom.Shopping_Cart.Model.Product;
import com.ecom.Shopping_Cart.Services.CategoryService;
import com.ecom.Shopping_Cart.Services.ProductServices;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductServices productServices;
    @GetMapping("/")
    public String index(){
        return "admin/index";
    }


    @GetMapping("/loadAddProduct")
    public String loadAddProduct(Model m){
        List<Category> categories =categoryService.getAllCategory();
        m.addAttribute("categories", categories);
        return "admin/add_product";
    }


    @GetMapping("/category")
    public String category(Model m){
        m.addAttribute("categorys",categoryService.getAllCategory());
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

    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable int id, HttpSession session){
        Boolean deleteCategory = categoryService.deleteCategory(id);
        if (deleteCategory){
          session.setAttribute("succMsg","Category Deleted Successfully");
        }else{
            session.setAttribute("errorMsg","Not Deleted ! Internal Server error");
        }
        return "redirect:/admin/category";
    }

    @GetMapping("/loadEditCategory/{id}")
    public String loadEditCategory(@PathVariable int id,Model m){
        m.addAttribute("editCategory",categoryService.getCategory(id));
        return "admin/edit_category";
    }

    @PostMapping("/updateCategory")
    public String updateCategory(@ModelAttribute Category category, @RequestParam("file")MultipartFile file, HttpSession session) throws IOException {
        Category oldCategory = categoryService.getCategory(category.getId());

        String imageName= file.isEmpty() ? oldCategory.getImageName() : file.getOriginalFilename();
        oldCategory.setName(category.getName());
        oldCategory.setIsActive(category.getIsActive());
        oldCategory.setImageName(imageName);

        Category updateCategory = categoryService.saveCategory(oldCategory);

        if (!ObjectUtils.isEmpty(updateCategory)){
            if (!file.isEmpty()){
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+"category_img"+File.separator+file.getOriginalFilename());

                System.out.println(path);
                Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
            }
            session.setAttribute("succMsg","Category Updated Successfully");
        }else{
            session.setAttribute("errorMsg","Not Saved ! Internal Server error");
        }
        return "redirect:/admin/loadEditCategory/"+category.getId();
    }


//    code for the product start from here

    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute Product product, @RequestParam("file")MultipartFile file, HttpSession session) throws IOException {

        String imageName = file.isEmpty()? "default.jpg" : file.getOriginalFilename();
        product.setDiscount(0);
        product.setDiscountPrice(product.getPrice());
        product.setImage(imageName);

        Product saveProduct = productServices.saveProduct(product);

        if (!ObjectUtils.isEmpty(saveProduct)){
            session.setAttribute("succMsg","Product Saved Successfully");
            File saveFile = new ClassPathResource("static/img").getFile();
            Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+"product_img"+File.separator+file.getOriginalFilename());

            System.out.println(path);
            Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
        }{
            session.setAttribute("errorMsg","Product Saved Successfully");
        }

        return "redirect:/admin/loadAddProduct";
    }

    @GetMapping("products")
    public  String loadViewProduct(Model m){
      List<Product> products = productServices.getAllProducts();
      m.addAttribute("products",products);
    return "admin/product";}

    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable int id, HttpSession session){
        Boolean deleteCategory = productServices.deleteProduct(id);
        if (deleteCategory){
            session.setAttribute("succMsg","Product Deleted Successfully");
        }else{
            session.setAttribute("errorMsg","Not Deleted ! Internal Server error");
        }
        return "redirect:/admin/products";
    }


    @GetMapping("/loadEditProduct/{id}")
    public String loadEditProduct(@PathVariable int id,Model m) throws IOException {

        Product product = productServices.getProduct(id);

        m.addAttribute("editProduct",product);
        m.addAttribute("categories",categoryService.getAllCategory());

      return "admin/edit_product";
    };

    @PostMapping("/updateProduct")
    public String updateProduct(@ModelAttribute Product product, @RequestParam("file")MultipartFile file, HttpSession session) throws IOException {

        Product oldProduct = productServices.getProduct(product.getId());
        String image = file.isEmpty()? oldProduct.getImage() : file.getOriginalFilename();
        oldProduct.setImage(image);
        oldProduct.setTitle(product.getTitle());
        oldProduct.setDescription(product.getDescription());
        oldProduct.setPrice(product.getPrice());
        oldProduct.setCategory(product.getCategory());
        oldProduct.setPrice(product.getPrice());
        oldProduct.setStock(product.getStock());
        oldProduct.setDiscount(product.getDiscount());
        oldProduct.setIsActive(product.getIsActive());

        Double discount = product.getPrice()*(product.getDiscount()/100.0);
        Double discountPrice = product.getPrice()-discount;
        oldProduct.setDiscountPrice(discountPrice);

        if (product.getDiscount()<0 || product.getDiscount()>100){
            session.setAttribute("errorMsg","Invalid Discount");
        }else{
            Product updateProduct = productServices.saveProduct(oldProduct);
            if (!ObjectUtils.isEmpty(updateProduct)){
                session.setAttribute("succMsg","Product Updated Successfully");
                if (!file.isEmpty()){
                    File saveFile = new ClassPathResource("static/img").getFile();
                    Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+"product_img"+File.separator+file.getOriginalFilename());

                    System.out.println(path);
                    Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
                }
            }else{
                session.setAttribute("errorMsg","Not Saved ! Internal Server error");
            }
        }


    return "redirect:/admin/loadEditProduct/"+product.getId();
    }
}
