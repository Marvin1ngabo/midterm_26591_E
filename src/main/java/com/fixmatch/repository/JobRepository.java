package com.fixmatch.repository;

import com.fixmatch.entity.Job;
import com.fixmatch.entity.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * JobRepository - Repository for Job entity
 */
@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    /**
     * Find jobs by client ID with Pagination
     */
    Page<Job> findByClientId(Long clientId, Pageable pageable);

    /**
     * Find jobs by provider ID with Pagination
     */
    Page<Job> findByProviderId(Long providerId, Pageable pageable);

    /**
     * Find jobs by status with Pagination and Sorting
     * 
     * Example: Find all OPEN jobs sorted by created date
     */
    Page<Job> findByStatus(JobStatus status, Pageable pageable);

    /**
     * Find jobs by category with Pagination
     */
    Page<Job> findByCategoryId(Long categoryId, Pageable pageable);

    /**
     * Find jobs by province code
     */
    @Query("SELECT j FROM Job j JOIN j.location l WHERE l.provinceCode = :provinceCode")
    List<Job> findJobsByProvinceCode(@Param("provinceCode") String provinceCode);

    /**
     * Find jobs by province code with Pagination
     */
    @Query("SELECT j FROM Job j JOIN j.location l WHERE l.provinceCode = :provinceCode")
    Page<Job> findJobsByProvinceCode(@Param("provinceCode") String provinceCode, Pageable pageable);

    /**
     * Find open jobs by category and province
     */
    @Query("SELECT j FROM Job j JOIN j.location l " +
           "WHERE j.category.id = :categoryId " +
           "AND l.provinceCode = :provinceCode " +
           "AND j.status = 'OPEN'")
    Page<Job> findOpenJobsByCategoryAndProvince(
        @Param("categoryId") Long categoryId,
        @Param("provinceCode") String provinceCode,
        Pageable pageable
    );

    /**
     * Find jobs by budget range
     */
    @Query("SELECT j FROM Job j WHERE j.budget BETWEEN :minBudget AND :maxBudget")
    Page<Job> findJobsByBudgetRange(
        @Param("minBudget") java.math.BigDecimal minBudget,
        @Param("maxBudget") java.math.BigDecimal maxBudget,
        Pageable pageable
    );

    /**
     * Find available jobs (OPEN status) with pagination
     */
    @Query("SELECT j FROM Job j WHERE j.status = 'OPEN' ORDER BY j.createdAt DESC")
    Page<Job> findAvailableJobs(Pageable pageable);

    /**
     * Find jobs by title containing keyword (case-insensitive)
     */
    @Query("SELECT j FROM Job j WHERE LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Job> searchJobsByTitle(@Param("keyword") String keyword, Pageable pageable);

    /**
     * Count jobs by status
     */
    long countByStatus(JobStatus status);

    /**
     * Count jobs by client
     */
    long countByClientId(Long clientId);

    /**
     * Count completed jobs by provider
     */
    long countByProviderIdAndStatus(Long providerId, JobStatus status);

    /**
     * Find recent jobs (created in last N days)
     */
    @Query("SELECT j FROM Job j WHERE j.createdAt >= :sinceDate ORDER BY j.createdAt DESC")
    List<Job> findRecentJobs(@Param("sinceDate") java.time.LocalDateTime sinceDate);

    /**
     * Find jobs by location
     */
    Page<Job> findByLocationId(Long locationId, Pageable pageable);

    /**
     * Find high-budget jobs (above specified amount)
     */
    @Query("SELECT j FROM Job j WHERE j.budget >= :minBudget ORDER BY j.budget DESC")
    Page<Job> findHighBudgetJobs(@Param("minBudget") java.math.BigDecimal minBudget, Pageable pageable);

    /**
     * Find jobs by multiple statuses
     */
    @Query("SELECT j FROM Job j WHERE j.status IN :statuses")
    Page<Job> findJobsByStatuses(@Param("statuses") List<JobStatus> statuses, Pageable pageable);

    /**
     * Get job statistics by province
     */
    @Query("SELECT l.provinceName, COUNT(j) FROM Job j JOIN j.location l GROUP BY l.provinceName")
    List<Object[]> getJobStatsByProvince();
}
