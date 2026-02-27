package com.fixmatch.service;

import com.fixmatch.entity.ServiceCategory;
import com.fixmatch.repository.ServiceCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * ServiceCategoryService - Business logic for ServiceCategory entity
 */
@Service
@Transactional
public class ServiceCategoryService {

    @Autowired
    private ServiceCategoryRepository categoryRepository;

    /**
     * Create new category
     */
    public ServiceCategory createCategory(ServiceCategory category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new RuntimeException("Category already exists: " + category.getName());
        }
        return categoryRepository.save(category);
    }

    /**
     * Get all categories with Sorting
     * 
     * Example: Sort by name or provider count
     */
    public List<ServiceCategory> getAllCategories(Sort sort) {
        return categoryRepository.findAll(sort);
    }

    /**
     * Get all categories
     */
    public List<ServiceCategory> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Get category by ID
     */
    public ServiceCategory getCategoryById(Long id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    /**
     * Get category by name
     */
    public ServiceCategory getCategoryByName(String name) {
        return categoryRepository.findByName(name)
            .orElseThrow(() -> new RuntimeException("Category not found: " + name));
    }
}
