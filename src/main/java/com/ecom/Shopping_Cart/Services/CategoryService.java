package com.ecom.Shopping_Cart.Services;

import com.ecom.Shopping_Cart.Model.Category;

import java.util.List;

public interface CategoryService {

    public Category saveCategory(Category category) ;

    public List<Category> getAllCategory();

    public Boolean existCategory(String name);

    public Boolean deleteCategory(int id);

    public Category getCategory(int id);
}
