package com.fixmatch.repository;

import com.fixmatch.entity.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * LocationRepository - Repository for Location entity
 * 
 * Demonstrates:
 * 1. existsBy() methods (Assessment Requirement #7)
 * 2. Custom queries for hierarchical data
 * 3. Pagination and Sorting (Assessment Requirement #3)
 */
@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    /**
     * REQUIREMENT #7: existsBy() methods
     */
    boolean existsByProvinceCode(String provinceCode);
    
    boolean existsByProvinceCodeAndDistrictName(String provinceCode, String districtName);

    /**
     * Find locations by province code
     */
    List<Location> findByProvinceCode(String provinceCode);

    /**
     * Find locations by province code with pagination
     */
    Page<Location> findByProvinceCode(String provinceCode, Pageable pageable);

    /**
     * Find locations by province name
     */
    List<Location> findByProvinceName(String provinceName);

    /**
     * Find locations by district name
     */
    List<Location> findByDistrictName(String districtName);

    /**
     * Find locations by province and district
     */
    List<Location> findByProvinceCodeAndDistrictName(String provinceCode, String districtName);

    /**
     * Get all unique provinces
     */
    @Query("SELECT DISTINCT l.provinceCode, l.provinceName FROM Location l ORDER BY l.provinceName")
    List<Object[]> findAllProvinces();

    /**
     * Get all districts in a province
     */
    @Query("SELECT DISTINCT l.districtName FROM Location l WHERE l.provinceCode = :provinceCode ORDER BY l.districtName")
    List<String> findDistrictsByProvinceCode(@Param("provinceCode") String provinceCode);

    /**
     * Get all sectors in a district
     */
    @Query("SELECT DISTINCT l.sectorName FROM Location l WHERE l.provinceCode = :provinceCode AND l.districtName = :districtName AND l.sectorName IS NOT NULL ORDER BY l.sectorName")
    List<String> findSectorsByProvinceAndDistrict(@Param("provinceCode") String provinceCode, @Param("districtName") String districtName);

    /**
     * Find locations with complete hierarchy (all levels filled)
     */
    @Query("SELECT l FROM Location l WHERE l.villageName IS NOT NULL")
    List<Location> findCompleteHierarchyLocations();

    /**
     * Find locations by level (District, Sector, Cell, Village)
     */
    @Query("SELECT l FROM Location l WHERE " +
           "CASE " +
           "  WHEN l.villageName IS NOT NULL THEN 'Village' " +
           "  WHEN l.cellName IS NOT NULL THEN 'Cell' " +
           "  WHEN l.sectorName IS NOT NULL THEN 'Sector' " +
           "  ELSE 'District' " +
           "END = :level")
    List<Location> findByLocationLevel(@Param("level") String level);

    /**
     * Search locations by any field containing keyword
     */
    @Query("SELECT l FROM Location l WHERE " +
           "LOWER(l.provinceName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(l.districtName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(l.sectorName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(l.cellName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(l.villageName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Location> searchByKeyword(@Param("keyword") String keyword);

    /**
     * Count locations by province
     */
    long countByProvinceCode(String provinceCode);

    /**
     * Find location by complete address
     */
    Optional<Location> findByProvinceCodeAndDistrictNameAndSectorNameAndCellNameAndVillageName(
        String provinceCode, String districtName, String sectorName, String cellName, String villageName);
}