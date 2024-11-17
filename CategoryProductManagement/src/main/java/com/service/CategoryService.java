package com.service;

import com.entity.Category;

import java.util.Map;


public interface CategoryService {
    Category createCategory(Category category);
    Category getCategoryById(Long id);
    Category updateCategory(Long id, Category category);
    void deleteCategory(Long id);
	Map<String, Object> getNextPageCategory(int page);
}
