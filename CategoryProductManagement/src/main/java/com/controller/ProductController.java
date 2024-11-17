package com.controller;

import com.entity.Product;
import com.serviceimpl.ProductServiceImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductServiceImpl productServiceImpl;

    // Get All data Using Pagination
    @GetMapping
    public Map<String, Object> getNextPageProduct(@RequestParam(defaultValue = "2") int page) {
        return productServiceImpl.getNextPageProduct(page);
    }

    // Get data Using Id
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productServiceImpl.getProductById(id);
    }

    //Post Data
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productServiceImpl.createProduct(product);
    }

    //Update data using ID
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        if (productServiceImpl.getProductById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productServiceImpl.updateProduct(id, product));
    }

    //Delete data using ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        if (productServiceImpl.getProductById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        productServiceImpl.deleteProduct(id);
        return ResponseEntity.ok("Deleted Sucessfully");
    }
}
