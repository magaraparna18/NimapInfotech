package com.serviceimpl;

import com.dto.ProductDto;
import com.entity.Product;
import com.repository.ProductRepository;
import com.service.ProductService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
	    private int currentPage = 0;
	    private boolean firstPageHit = true;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        Product existing = getProductById(id);
        existing.setName(product.getName());
        existing.setCategory(product.getCategory());
        return productRepository.save(existing);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    // Pagination
    @Override
    public Map<String, Object> getNextPageProduct(int page) {
        Map<String, Object> response = new HashMap<>();

     // Add total Product count on the first page hit
        if (firstPageHit) {
            long totalProduct = productRepository.count();
            response.put("totalProduct", totalProduct);
            firstPageHit = false;
        }

        // Create pageable object
        Pageable pageable = PageRequest.of(currentPage, page);

        // Fetch Product in pages
        Page<Product> productPage = productRepository.findAll(pageable);

        // Convert Product objects to ProductDTO objects
        List<ProductDto> productDTOs = productPage.getContent().stream().map(product -> {
        	ProductDto dto = new ProductDto();
            dto.setName(product.getName());
            dto.setId(product.getId());
            dto.setPrice(product.getPrice());
            
            return dto;
        }).collect(Collectors.toList());

        // Add Product list to response
        response.put("product", productDTOs);

        // Update page or reset and add "End of Product" message
        if (productPage.hasNext()) {
            currentPage++;
        } else {
            currentPage = 0; // Reset to the first page
            firstPageHit = true; // Reset for next cycle
            response.put("message", "End of product.");
        }

        return response;
    }

}
