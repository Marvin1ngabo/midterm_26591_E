package com.fixmatch.repository;

import com.fixmatch.entity.District;
import com.fixmatch.entity.Province;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * DistrictRepository - Repository interface for District entity
 * 
 * Demonstrates:
 * 1. Pagination support
 * 2. Sorting support
 * 3. Querying by relationships
 */
@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {

    /**
     * Find all districts by province
     * 
     * Logic: SELECT * FROM districts WHERE province_id = ?
     */
    List<District> findByProvince(Province province);

    /**
     * Find all districts by province ID
     */
    List<District> findByProvinceId(Long provinceId);

    /**
     * Find districts by province with Pagination
     * 
     * Logic: Pagination allows fetching data in chunks
     * Example: Page 0, Size 10 = First 10 districts
     *          Page 1, Size 10 = Next 10 districts
     * 
     * Benefits:
     * - Reduces memory usage
     * - Improves performance
     * - Better user experience (load data as needed)
     */
    Page<District> findByProvinceId(Long provinceId, Pageable pageable);

    /**
     * Check if district exists by name
     */
    boolean existsByName(String name);
}
