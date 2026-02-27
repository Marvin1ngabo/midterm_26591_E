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
    @Query("SELECT j FROM Job j JOIN j.district d JOIN d.province p WHERE p.code = :provinceCode")
    List<Job> findJobsByProvinceCode(@Param("provinceCode") String provinceCode);

    /**
     * Find jobs by province code with Pagination
     */
    @Query("SELECT j FROM Job j JOIN j.district d JOIN d.province p WHERE p.code = :provinceCode")
    Page<Job> findJobsByProvinceCode(@Param("provinceCode") String provinceCode, Pageable pageable);

    /**
     * Find open jobs by category and province
     */
    @Query("SELECT j FROM Job j JOIN j.district d JOIN d.province p " +
           "WHERE j.category.id = :categoryId " +
           "AND p.code = :provinceCode " +
           "AND j.status = 'OPEN'")
    Page<Job> findOpenJobsByCategoryAndProvince(
        @Param("categoryId") Long categoryId,
        @Param("provinceCode") String provinceCode,
        Pageable pageable
    );
}
