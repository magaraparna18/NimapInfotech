package com.controller;

import com.entity.Category;
import com.serviceimpl.CategoryServiceImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryServiceImpl categoryServiceImpl;

    
    // Get All data Using Pagination
    @GetMapping
    public Map<String, Object> getNextPageCategory(@RequestParam(defaultValue = "2") int page) {
        return categoryServiceImpl.getNextPageCategory(page);
    }

  //Get Data Using ID
    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable Long id) {
        return categoryServiceImpl.getCategoryById(id);
    }
    
    //Post Data
    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return categoryServiceImpl.createCategory(category);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        if (categoryServiceImpl.getCategoryById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(categoryServiceImpl.updateCategory(id, category));
    }

    //Delete Data Using ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        if (categoryServiceImpl.getCategoryById(id) == null) {
            return ResponseEntity.ok("Not present in database");
        }
        categoryServiceImpl.deleteCategory(id);
        return ResponseEntity.ok("Deleted Sucessfully");
    }
}
