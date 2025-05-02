package com.ecom.Shopping_Cart.Controller;


import com.ecom.Shopping_Cart.Model.Category;
import com.ecom.Shopping_Cart.Model.Product;
import com.ecom.Shopping_Cart.Model.ProductOrder;
import com.ecom.Shopping_Cart.Model.UserDtls;
import com.ecom.Shopping_Cart.Services.*;
import com.ecom.Shopping_Cart.Utils.CommonUtils;
import com.ecom.Shopping_Cart.Utils.OrderStatus;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
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
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderServices orderServices;

    @Autowired
    private CommonUtils commonUtils;


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
    public String category(Model m,@RequestParam(name = "pageNo",defaultValue = "0")Integer pageNo,
                           @RequestParam(name = "pageSize",defaultValue = "2")Integer pageSize){
        Page<Category> page = categoryService.getAllCategoryPagination(pageNo,pageSize);
        List<Category> categorys = page.getContent();
        m.addAttribute("categorys", categorys);
        m.addAttribute("totalPages", page.getTotalPages());
        m.addAttribute("pageNo", page.getNumber());
        m.addAttribute("pageSize", pageSize);
        m.addAttribute("totalElements", page.getTotalElements());
        m.addAttribute("isFirst", page.isFirst());
        m.addAttribute("isLast", page.isLast());
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
    public  String loadViewProduct(Model m,@RequestParam(defaultValue = "") String ch,@RequestParam(name = "pageNo",defaultValue = "0")Integer pageNo,
                                   @RequestParam(name = "pageSize",defaultValue = "3")Integer pageSize){
        Page<Product> pageproducts=null;
        if (ch!=null && ch.length()>0){
            pageproducts = productServices.searchProductsPagination(ch,pageNo,pageSize);
        }else{
            pageproducts = productServices.getAllProductsPagination(pageNo,pageSize);
        }
        List<Product> products = pageproducts.getContent();
        m.addAttribute("products",products);
        m.addAttribute("totalPages", pageproducts.getTotalPages());
        m.addAttribute("pageNo", pageproducts.getNumber());
        m.addAttribute("pageSize", pageSize);
        m.addAttribute("totalElements", pageproducts.getTotalElements());
        m.addAttribute("isFirst", pageproducts.isFirst());
        m.addAttribute("isLast", pageproducts.isLast());
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


    //User Code here

    @GetMapping("/users")
    public String getAllUsers(Model m){
        List<UserDtls> allUser = userService.getAllUser("ROLE_USER");
        m.addAttribute("users",allUser);
        return "admin/users";

    }
@GetMapping("/updateStatus/{id}")
    public String updateUserAccountStatus(@PathVariable int id,HttpSession session){
        Boolean f  = userService.updateAccountStatus(id);
        System.out.println(String.valueOf(id));
        if (f){
            session.setAttribute("succMsg","Account Status Updated Successfully");
        }else{
            session.setAttribute("errorMsg","Not Updated ! Internal Server error");
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/orders")
    public String getAllOrders(Principal p , Model m,@RequestParam(name = "pageNo",defaultValue = "0")Integer pageNo,
                               @RequestParam(name = "pageSize",defaultValue = "3")Integer pageSize) {
        Page<ProductOrder>  orders = orderServices.getAllOrdersPagination(pageNo,pageSize);
        m.addAttribute("orders",orders.getContent());
        m.addAttribute("srch",false);
        m.addAttribute("totalPages", orders.getTotalPages());
        m.addAttribute("pageNo", orders.getNumber());
        m.addAttribute("pageSize", pageSize);
        m.addAttribute("totalElements", orders.getTotalElements());
        m.addAttribute("isFirst", orders.isFirst());
        m.addAttribute("isLast", orders.isLast());

        return "/admin/order";
    }

    @PostMapping("/updateOrderStatus")
    public String updateOrderStatus(@RequestParam Integer id, @RequestParam Integer st,HttpSession session){
        OrderStatus[]   values = OrderStatus.values();
        String status = null;

        for (OrderStatus os : values) {
            if (os.getId().equals(st))
            {
                status = os.getName();
            }

        }

        ProductOrder updateOrder =  orderServices.updateOrderStatus(id,status);
        try {
            commonUtils.sendMailForProductOrder(updateOrder,status);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (!ObjectUtils.isEmpty(updateOrder)){
            session.setAttribute("succMsg", "Status updated");
        }else{
            session.setAttribute("errorMsg", "Something went wrong on server");
        }
        return "redirect:/admin/orders";
    }

    public UserDtls getLoggedInUserDetail(Principal p){
        UserDtls user = userService.getUserByEmail(p.getName());
        return user;
    }

    //code for the search order via order id ;
    @GetMapping("/searchOrder")
    public String  searchProduct(@RequestParam(defaultValue = "") String orderId,Model m,HttpSession session,@RequestParam(name = "pageNo",defaultValue = "0")Integer pageNo,
                                 @RequestParam(name = "pageSize",defaultValue = "3")Integer pageSize){

        if (orderId!=null && orderId.length()>0){
            ProductOrder order = orderServices.getOrderById(orderId.trim());

            if (ObjectUtils.isEmpty(order)){
                session.setAttribute("errorMsg","Order Not Found");
            }else{
                m.addAttribute("orderDtls",order);
                m.addAttribute("srch",true);
            }
        }else{
//            List<ProductOrder>  orders = orderServices.getAllOrders();
//            m.addAttribute("orders",orders);
//            m.addAttribute("srch",false);
            Page<ProductOrder>  orders = orderServices.getAllOrdersPagination(pageNo,pageSize);
            m.addAttribute("orders",orders.getContent());
            m.addAttribute("srch",false);
            m.addAttribute("totalPages", orders.getTotalPages());
            m.addAttribute("pageNo", orders.getNumber());
            m.addAttribute("pageSize", pageSize);
            m.addAttribute("totalElements", orders.getTotalElements());
            m.addAttribute("isFirst", orders.isFirst());
            m.addAttribute("isLast", orders.isLast());
        }
        return "admin/order";
    }


}
