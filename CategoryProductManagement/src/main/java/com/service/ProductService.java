package com.service;

import com.entity.Product;

import java.util.Map;

public interface ProductService {
    Product getProductById(Long id);
    Product createProduct(Product product);
    Product updateProduct(Long id, Product product);
    void deleteProduct(Long id);
    Map<String, Object> getNextPageProduct(int page);

}
