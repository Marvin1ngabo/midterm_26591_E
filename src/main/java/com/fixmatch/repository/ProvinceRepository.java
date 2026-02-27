package com.fixmatch.repository;

import com.fixmatch.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * ProvinceRepository - Repository interface for Province entity
 * 
 * Extends JpaRepository which provides:
 * - save(), findById(), findAll(), delete(), etc.
 * - Pagination and sorting support
 * 
 * Custom methods demonstrate:
 * 1. existsBy() method - Check if province exists
 * 2. findBy() methods - Query by specific fields
 */
@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {

    /**
     * existsBy() Method - Checks if a province exists by code
     * 
     * Logic: Spring Data JPA automatically generates the query:
     * SELECT COUNT(*) > 0 FROM provinces WHERE code = ?
     * 
     * Returns: true if province exists, false otherwise
     */
    boolean existsByCode(String code);

    /**
     * existsBy() Method - Checks if a province exists by name
     */
    boolean existsByName(String name);

    /**
     * findBy() Method - Find province by code
     * 
     * Logic: SELECT * FROM provinces WHERE code = ?
     * Returns: Optional<Province> (empty if not found)
     */
    Optional<Province> findByCode(String code);

    /**
     * findBy() Method - Find province by name
     */
    Optional<Province> findByName(String name);

    /**
     * findBy() Method - Find province by name (case-insensitive)
     * 
     * Logic: SELECT * FROM provinces WHERE LOWER(name) = LOWER(?)
     */
    Optional<Province> findByNameIgnoreCase(String name);
}
