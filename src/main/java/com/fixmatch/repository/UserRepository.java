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
     * 
     * Logic:
     * - Join User → District → Province
     * - Filter by province code
     * 
     * JPQL Query:
     * SELECT u FROM User u 
     * JOIN u.district d 
     * JOIN d.province p 
     * WHERE p.code = :provinceCode
     */
    @Query("SELECT u FROM User u JOIN u.district d JOIN d.province p WHERE p.code = :provinceCode")
    List<User> findUsersByProvinceCode(@Param("provinceCode") String provinceCode);

    /**
     * REQUIREMENT #8: Retrieve all users from a given province using province NAME
     * 
     * Logic: Same as above but filter by province name
     */
    @Query("SELECT u FROM User u JOIN u.district d JOIN d.province p WHERE p.name = :provinceName")
    List<User> findUsersByProvinceName(@Param("provinceName") String provinceName);

    /**
     * Find providers by province code with Pagination
     * 
     * This combines:
     * - Province filtering
     * - User type filtering
     * - Pagination
     */
    @Query("SELECT u FROM User u JOIN u.district d JOIN d.province p " +
           "WHERE p.code = :provinceCode AND u.userType IN ('PROVIDER', 'BOTH')")
    Page<User> findProvidersByProvinceCode(@Param("provinceCode") String provinceCode, Pageable pageable);

    /**
     * Find providers by province name with Pagination
     */
    @Query("SELECT u FROM User u JOIN u.district d JOIN d.province p " +
           "WHERE p.name = :provinceName AND u.userType IN ('PROVIDER', 'BOTH')")
    Page<User> findProvidersByProvinceName(@Param("provinceName") String provinceName, Pageable pageable);

    /**
     * Find users by district
     */
    List<User> findByDistrictId(Long districtId);

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
}
