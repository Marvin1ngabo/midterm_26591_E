package com.fixmatch.repository;

import com.fixmatch.entity.Location;
import com.fixmatch.entity.LocationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * LocationRepository - Repository for hierarchical location operations
 * 
 * Demonstrates:
 * 1. Tree traversal queries
 * 2. Hierarchical data retrieval
 * 3. Adjacency List Model operations
 */
@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    /**
     * Find all root locations (provinces)
     */
    List<Location> findByParentLocationIsNull();

    /**
     * Find all locations of a specific type
     */
    List<Location> findByType(LocationType type);

    /**
     * Find location by name and type
     */
    Optional<Location> findByNameAndType(String name, LocationType type);

    /**
     * Find location by code
     */
    Optional<Location> findByCode(String code);

    /**
     * Find all children of a specific location
     */
    List<Location> findByParentLocation(Location parent);

    /**
     * Find all children of a specific location by parent ID
     */
    List<Location> findByParentLocationLocationId(Long parentId);

    /**
     * Find all locations under a specific parent (recursive)
     * This uses a recursive CTE (Common Table Expression) to get all descendants
     */
    @Query(value = """
        WITH RECURSIVE location_tree AS (
            -- Base case: start with the specified parent
            SELECT location_id, name, code, type, parent_location_id, 0 as level
            FROM locations 
            WHERE location_id = :parentId
            
            UNION ALL
            
            -- Recursive case: find all children
            SELECT h.location_id, h.name, h.code, h.type, h.parent_location_id, lt.level + 1
            FROM locations h
            INNER JOIN location_tree lt ON h.parent_location_id = lt.location_id
        )
        SELECT * FROM location_tree WHERE level > 0 ORDER BY level, name
        """, nativeQuery = true)
    List<Object[]> findAllDescendants(@Param("parentId") Long parentId);

    /**
     * Find all ancestors of a specific location (path to root)
     */
    @Query(value = """
        WITH RECURSIVE location_path AS (
            -- Base case: start with the specified location
            SELECT location_id, name, code, type, parent_location_id, 0 as level
            FROM locations 
            WHERE location_id = :locationId
            
            UNION ALL
            
            -- Recursive case: find parent
            SELECT h.location_id, h.name, h.code, h.type, h.parent_location_id, lp.level + 1
            FROM locations h
            INNER JOIN location_path lp ON h.location_id = lp.parent_location_id
        )
        SELECT * FROM location_path WHERE level > 0 ORDER BY level DESC
        """, nativeQuery = true)
    List<Object[]> findAllAncestors(@Param("locationId") Long locationId);

    /**
     * Find locations by name (case-insensitive search)
     */
    List<Location> findByNameContainingIgnoreCase(String name);

    /**
     * Find all villages (leaf nodes)
     */
    @Query("SELECT h FROM Location h WHERE h.type = :villageType")
    List<Location> findAllVillages(@Param("villageType") LocationType villageType);

    /**
     * Find all provinces (root nodes)
     */
    @Query("SELECT h FROM Location h WHERE h.type = :provinceType")
    List<Location> findAllProvinces(@Param("provinceType") LocationType provinceType);

    /**
     * Check if location exists by name and type
     */
    boolean existsByNameAndType(String name, LocationType type);

    /**
     * Count children of a specific location
     */
    long countByParentLocation(Location parent);

    /**
     * Find locations by type under a specific parent
     */
    @Query("SELECT h FROM Location h WHERE h.parentLocation.locationId = :parentId AND h.type = :type")
    List<Location> findByParentIdAndType(@Param("parentId") Long parentId, @Param("type") LocationType type);

    /**
     * Get full hierarchy tree starting from a specific location
     */
    @Query("SELECT h FROM Location h LEFT JOIN FETCH h.childLocations WHERE h.locationId = :locationId")
    Optional<Location> findWithChildren(@Param("locationId") Long locationId);
}