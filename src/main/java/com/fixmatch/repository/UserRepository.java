package com.fixmatch.repository;

import com.fixmatch.entity.User;
import com.fixmatch.entity.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * UserRepository - Repository interface for User entity
 * 
 * Demonstrates:
 * 1. existsBy() methods
 * 2. Pagination and Sorting
 * 3. Custom JPQL queries
 * 4. Querying by province (requirement #8)
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * existsBy() Methods - Check if user exists
     * 
     * Logic: These methods check for existence without loading the entire entity
     * More efficient than findBy() when you only need to check existence
     */
    boolean existsByEmail(String email);
    
    boolean existsByPhone(String phone);

    /**
     * Find user by email
     */
    Optional<User> findByEmail(String email);

    /**
     * Find users by type with Pagination and Sorting
     * 
     * Example usage:
     * - Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
     * - Page<User> providers = userRepository.findByUserType(UserType.PROVIDER, pageable);
     * 
     * Benefits:
     * - Pagination: Load 10 users at a time instead of all users
     * - Sorting: Order by name, rating, created date, etc.
     */
    Page<User> findByUserType(UserType userType, Pageable pageable);

    /**
     * Find all users with Sorting
     * 
     * Example usage:
     * - Sort sort = Sort.by("name").ascending();
     * - List<User> users = userRepository.findAll(sort);
     */
    List<User> findAll(Sort sort);

    /**
     * REQUIREMENT #8: Retrieve all users from a given province using province CODE
     * Simplified: Find users by location code (any level)
     */
    @Query("SELECT u FROM User u JOIN u.location l WHERE l.code = :provinceCode")
    List<User> findUsersByProvinceCode(@Param("provinceCode") String provinceCode);

    /**
     * REQUIREMENT #8: Retrieve all users from a given province using province NAME
     * Uses hierarchical location traversal to find users in any location within the province
     */
    @Query("SELECT u FROM User u JOIN u.location l " +
           "WHERE l.name = :provinceName AND l.type = 'PROVINCE' " +
           "OR (l.parentLocation.name = :provinceName AND l.parentLocation.type = 'PROVINCE') " +
           "OR (l.parentLocation.parentLocation.name = :provinceName AND l.parentLocation.parentLocation.type = 'PROVINCE') " +
           "OR (l.parentLocation.parentLocation.parentLocation.name = :provinceName AND l.parentLocation.parentLocation.parentLocation.type = 'PROVINCE') " +
           "OR (l.parentLocation.parentLocation.parentLocation.parentLocation.name = :provinceName AND l.parentLocation.parentLocation.parentLocation.parentLocation.type = 'PROVINCE')")
    List<User> findUsersByProvinceName(@Param("provinceName") String provinceName);

    /**
     * Find providers by province code with Pagination
     * Simplified version
     */
    @Query("SELECT u FROM User u JOIN u.location l " +
           "WHERE u.userType IN ('PROVIDER', 'BOTH') AND l.code = :provinceCode")
    Page<User> findProvidersByProvinceCode(@Param("provinceCode") String provinceCode, Pageable pageable);

    /**
     * Find providers by province name with Pagination
     * Uses hierarchical location traversal
     */
    @Query("SELECT u FROM User u JOIN u.location l " +
           "WHERE u.userType IN ('PROVIDER', 'BOTH') AND (" +
           "(l.name = :provinceName AND l.type = 'PROVINCE') " +
           "OR (l.parentLocation.name = :provinceName AND l.parentLocation.type = 'PROVINCE') " +
           "OR (l.parentLocation.parentLocation.name = :provinceName AND l.parentLocation.parentLocation.type = 'PROVINCE') " +
           "OR (l.parentLocation.parentLocation.parentLocation.name = :provinceName AND l.parentLocation.parentLocation.parentLocation.type = 'PROVINCE') " +
           "OR (l.parentLocation.parentLocation.parentLocation.parentLocation.name = :provinceName AND l.parentLocation.parentLocation.parentLocation.parentLocation.type = 'PROVINCE'))")
    Page<User> findProvidersByProvinceName(@Param("provinceName") String provinceName, Pageable pageable);

    /**
     * Find users by location
     */
    List<User> findByLocationLocationId(Long locationId);

    /**
     * Find verified providers with pagination and sorting
     */
    @Query("SELECT u FROM User u JOIN u.providerProfile p " +
           "WHERE u.userType IN ('PROVIDER', 'BOTH') AND p.verificationStatus = true")
    Page<User> findVerifiedProviders(Pageable pageable);

    /**
     * Find providers by skill name
     */
    @Query("SELECT DISTINCT u FROM User u JOIN u.providerProfile p JOIN p.skills s " +
           "WHERE u.userType IN ('PROVIDER', 'BOTH') AND s.name = :skillName")
    List<User> findProvidersBySkill(@Param("skillName") String skillName);

    /**
     * Find top-rated providers with minimum rating
     */
    @Query("SELECT u FROM User u JOIN u.providerProfile p " +
           "WHERE u.userType IN ('PROVIDER', 'BOTH') AND p.rating >= :minRating " +
           "ORDER BY p.rating DESC")
    List<User> findTopRatedProviders(@Param("minRating") Double minRating);

    /**
     * Count users by user type
     */
    long countByUserType(UserType userType);

    /**
     * Find users created in the last N days
     */
    @Query("SELECT u FROM User u WHERE u.createdAt >= :sinceDate")
    List<User> findRecentUsers(@Param("sinceDate") java.time.LocalDateTime sinceDate);

    /**
     * Find providers with experience greater than specified years
     */
    @Query("SELECT u FROM User u JOIN u.providerProfile p " +
           "WHERE u.userType IN ('PROVIDER', 'BOTH') AND p.yearsExperience >= :minExperience")
    List<User> findExperiencedProviders(@Param("minExperience") Integer minExperience);

    /**
     * Search users by name (case-insensitive)
     */
    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<User> searchByName(@Param("name") String name);

    /**
     * Find users by district name - Uses hierarchical location traversal
     */
    @Query("SELECT u FROM User u JOIN u.location l " +
           "WHERE l.name = :districtName AND l.type = 'DISTRICT' " +
           "OR (l.parentLocation.name = :districtName AND l.parentLocation.type = 'DISTRICT') " +
           "OR (l.parentLocation.parentLocation.name = :districtName AND l.parentLocation.parentLocation.type = 'DISTRICT') " +
           "OR (l.parentLocation.parentLocation.parentLocation.name = :districtName AND l.parentLocation.parentLocation.parentLocation.type = 'DISTRICT')")
    List<User> findUsersByDistrictName(@Param("districtName") String districtName);

    /**
     * Find users by sector name - Uses hierarchical location traversal
     */
    @Query("SELECT u FROM User u JOIN u.location l " +
           "WHERE l.name = :sectorName AND l.type = 'SECTOR' " +
           "OR (l.parentLocation.name = :sectorName AND l.parentLocation.type = 'SECTOR') " +
           "OR (l.parentLocation.parentLocation.name = :sectorName AND l.parentLocation.parentLocation.type = 'SECTOR')")
    List<User> findUsersBySectorName(@Param("sectorName") String sectorName);

    /**
     * Find users by cell name - Uses hierarchical location traversal
     */
    @Query("SELECT u FROM User u JOIN u.location l " +
           "WHERE l.name = :cellName AND l.type = 'CELL' " +
           "OR (l.parentLocation.name = :cellName AND l.parentLocation.type = 'CELL')")
    List<User> findUsersByCellName(@Param("cellName") String cellName);

    /**
     * Find users by village name - Direct match since users are typically linked to villages
     */
    @Query("SELECT u FROM User u JOIN u.location l WHERE l.name = :villageName AND l.type = 'VILLAGE'")
    List<User> findUsersByVillageName(@Param("villageName") String villageName);

    /**
     * Find users by complete location hierarchy - Hierarchical approach
     * Searches from most specific (village) to least specific (province)
     */
    @Query("SELECT u FROM User u JOIN u.location l " +
           "WHERE (:villageName IS NOT NULL AND l.name = :villageName AND l.type = 'VILLAGE') " +
           "OR (:villageName IS NULL AND :cellName IS NOT NULL AND " +
           "    (l.name = :cellName AND l.type = 'CELL' OR l.parentLocation.name = :cellName AND l.parentLocation.type = 'CELL')) " +
           "OR (:villageName IS NULL AND :cellName IS NULL AND :sectorName IS NOT NULL AND " +
           "    (l.name = :sectorName AND l.type = 'SECTOR' OR l.parentLocation.name = :sectorName AND l.parentLocation.type = 'SECTOR' OR l.parentLocation.parentLocation.name = :sectorName AND l.parentLocation.parentLocation.type = 'SECTOR')) " +
           "OR (:villageName IS NULL AND :cellName IS NULL AND :sectorName IS NULL AND :districtName IS NOT NULL AND " +
           "    (l.name = :districtName AND l.type = 'DISTRICT' OR l.parentLocation.name = :districtName AND l.parentLocation.type = 'DISTRICT' OR l.parentLocation.parentLocation.name = :districtName AND l.parentLocation.parentLocation.type = 'DISTRICT' OR l.parentLocation.parentLocation.parentLocation.name = :districtName AND l.parentLocation.parentLocation.parentLocation.type = 'DISTRICT')) " +
           "OR (:villageName IS NULL AND :cellName IS NULL AND :sectorName IS NULL AND :districtName IS NULL AND :provinceName IS NOT NULL AND " +
           "    (l.name = :provinceName AND l.type = 'PROVINCE' OR l.parentLocation.name = :provinceName AND l.parentLocation.type = 'PROVINCE' OR l.parentLocation.parentLocation.name = :provinceName AND l.parentLocation.parentLocation.type = 'PROVINCE' OR l.parentLocation.parentLocation.parentLocation.name = :provinceName AND l.parentLocation.parentLocation.parentLocation.type = 'PROVINCE' OR l.parentLocation.parentLocation.parentLocation.parentLocation.name = :provinceName AND l.parentLocation.parentLocation.parentLocation.parentLocation.type = 'PROVINCE'))")
    List<User> findUsersByLocationHierarchy(
        @Param("provinceName") String provinceName,
        @Param("districtName") String districtName,
        @Param("sectorName") String sectorName,
        @Param("cellName") String cellName,
        @Param("villageName") String villageName
    );
}
