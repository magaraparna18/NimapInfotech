package com.serviceimpl;

import com.dto.CategoryDto;
import com.entity.Category;
import com.repository.CategoryRepository;
import com.service.CategoryService;

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
public class CategoryServiceImpl implements CategoryService {
    private int currentPage = 0;
    private int page = 2;  // Default to 2, but can be changed dynamically
    private boolean firstPageHit = true;

    @Autowired
    private CategoryRepository categoryRepository;

    // Fetch a category by ID
    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    // Create a new category
    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    // Update an existing category by ID
    @Override
    public Category updateCategory(Long id, Category category) {
        Category existingCategory = getCategoryById(id);
        existingCategory.setName(category.getName());
        return categoryRepository.save(existingCategory);
    }

    // Delete a category by ID
    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found for deletion");
        }
        categoryRepository.deleteById(id);
    }

    // Pagination with dynamic page size
    @Override
    public Map<String, Object> getNextPageCategory(int page) {
        Map<String, Object> response = new HashMap<>();

        // Use the pageSize passed in the API request
        this.page = page > 0 ? page : 2;

        // Add total Category count on the first page hit
        if (firstPageHit) {
            long totalCategory = categoryRepository.count();
            response.put("totalCategory", totalCategory);
            firstPageHit = false;
        }

        // Create pageable object with dynamic page size
        Pageable pageable = PageRequest.of(currentPage, this.page);

        // Fetch Category in pages
        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        // Convert Category objects to CategoryDTO objects
        List<CategoryDto> categoryDTOs = categoryPage.getContent().stream().map(category -> {
            CategoryDto dto = new CategoryDto();
            dto.setName(category.getName());
            dto.setId(category.getId());
            return dto;
        }).collect(Collectors.toList());

        // Add category list to response
        response.put("category", categoryDTOs);

        // Update page or reset and add "End of Category" message
        if (categoryPage.hasNext()) {
            currentPage++;
        } else {
            currentPage = 0; // Reset to the first page if needed
            firstPageHit = true; // Reset for next cycle
            response.put("message", "End of Category.");
        }

        return response;
    }
}
