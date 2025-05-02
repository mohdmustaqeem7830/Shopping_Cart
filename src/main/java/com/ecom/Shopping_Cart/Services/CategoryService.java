package com.ecom.Shopping_Cart.Services;

import com.ecom.Shopping_Cart.Model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {

    public Category saveCategory(Category category) ;

    public List<Category> getAllCategory();

    public Boolean existCategory(String name);

    public Boolean deleteCategory(int id);

    public Category getCategory(int id);

    public List<Category> getAllActiveCategory();

    public Page<Category>  getAllCategoryPagination(Integer pageNo,Integer pageSize);
}
