package com.fixmatch.repository;

import com.fixmatch.entity.ProviderProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * ProviderProfileRepository - Repository for ProviderProfile entity
 * 
 * Demonstrates:
 * 1. Querying Many-to-Many relationships
 * 2. Complex filtering with pagination
 */
@Repository
public interface ProviderProfileRepository extends JpaRepository<ProviderProfile, Long> {

    /**
     * Find provider profile by user ID
     */
    Optional<ProviderProfile> findByUserId(Long userId);

    /**
     * Find verified providers with Pagination and Sorting
     * 
     * Example: Sort by rating descending
     */
    Page<ProviderProfile> findByVerificationStatus(Boolean verified, Pageable pageable);

    /**
     * Find providers by minimum rating with Pagination
     */
    Page<ProviderProfile> findByRatingGreaterThanEqual(BigDecimal minRating, Pageable pageable);

    /**
     * Find providers by skill name (Many-to-Many query)
     * 
     * Logic: Query through the join table
     * SELECT pp FROM ProviderProfile pp 
     * JOIN pp.skills s 
     * WHERE s.name = :skillName
     */
    @Query("SELECT pp FROM ProviderProfile pp JOIN pp.skills s WHERE s.name = :skillName")
    List<ProviderProfile> findBySkillName(@Param("skillName") String skillName);

    /**
     * Find providers by skill name with Pagination
     */
    @Query("SELECT pp FROM ProviderProfile pp JOIN pp.skills s WHERE s.name = :skillName")
    Page<ProviderProfile> findBySkillName(@Param("skillName") String skillName, Pageable pageable);

    /**
     * Find providers by multiple criteria with Pagination
     * 
     * This demonstrates complex querying:
     * - Skill filtering (Many-to-Many)
     * - Province filtering (through User → District → Province)
     * - Rating filtering
     * - Verification status
     */
    @Query("SELECT DISTINCT pp FROM ProviderProfile pp " +
           "JOIN pp.skills s " +
           "JOIN pp.user u " +
           "JOIN u.district d " +
           "JOIN d.province p " +
           "WHERE s.name = :skillName " +
           "AND p.code = :provinceCode " +
           "AND pp.rating >= :minRating " +
           "AND pp.verificationStatus = true")
    Page<ProviderProfile> findProvidersBySkillAndProvinceAndRating(
        @Param("skillName") String skillName,
        @Param("provinceCode") String provinceCode,
        @Param("minRating") BigDecimal minRating,
        Pageable pageable
    );
}
