package com.fixmatch.repository;

import com.fixmatch.entity.ServiceCategory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * ServiceCategoryRepository - Repository for ServiceCategory entity
 */
@Repository
public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Long> {

    /**
     * Find category by name
     */
    Optional<ServiceCategory> findByName(String name);

    /**
     * Check if category exists by name
     */
    boolean existsByName(String name);

    /**
     * Find all categories with sorting
     * 
     * Example: Sort by name or provider count
     */
    List<ServiceCategory> findAll(Sort sort);
}
