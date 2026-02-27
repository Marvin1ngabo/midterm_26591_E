package com.fixmatch.service;

import com.fixmatch.entity.Job;
import com.fixmatch.entity.JobStatus;
import com.fixmatch.entity.User;
import com.fixmatch.entity.ServiceCategory;
import com.fixmatch.entity.District;
import com.fixmatch.repository.JobRepository;
import com.fixmatch.repository.UserRepository;
import com.fixmatch.repository.ServiceCategoryRepository;
import com.fixmatch.repository.DistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * JobService - Business logic for Job entity
 * 
 * Demonstrates:
 * 1. Multiple Many-to-One relationships
 * 2. Pagination and Sorting
 * 3. Status management
 */
@Service
@Transactional
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceCategoryRepository categoryRepository;

    @Autowired
    private DistrictRepository districtRepository;

    /**
     * Create new job
     * 
     * Logic:
     * - Validate client exists
     * - Validate category exists
     * - Validate district exists
     * - Set relationships
     * - Save job
     */
    public Job createJob(Job job, Long clientId, Long categoryId, Long districtId) {
        // Validate client
        User client = userRepository.findById(clientId)
            .orElseThrow(() -> new RuntimeException("Client not found with id: " + clientId));
        
        // Validate category
        ServiceCategory category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
        
        // Validate district
        District district = districtRepository.findById(districtId)
            .orElseThrow(() -> new RuntimeException("District not found with id: " + districtId));
        
        // Set relationships
        job.setClient(client);
        job.setCategory(category);
        job.setDistrict(district);
        job.setStatus(JobStatus.OPEN);
        
        return jobRepository.save(job);
    }

    /**
     * Assign provider to job
     */
    public Job assignProvider(Long jobId, Long providerId) {
        Job job = jobRepository.findById(jobId)
            .orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));
        
        User provider = userRepository.findById(providerId)
            .orElseThrow(() -> new RuntimeException("Provider not found with id: " + providerId));
        
        job.setProvider(provider);
        job.setStatus(JobStatus.IN_PROGRESS);
        
        return jobRepository.save(job);
    }

    /**
     * Complete job
     */
    public Job completeJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
            .orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));
        
        job.setStatus(JobStatus.COMPLETED);
        job.setCompletedAt(LocalDateTime.now());
        
        return jobRepository.save(job);
    }

    /**
     * Get jobs by status with Pagination and Sorting
     * 
     * Example: Get all OPEN jobs sorted by created date
     */
    public Page<Job> getJobsByStatus(JobStatus status, Pageable pageable) {
        return jobRepository.findByStatus(status, pageable);
    }

    /**
     * Get jobs by client with Pagination
     */
    public Page<Job> getJobsByClient(Long clientId, Pageable pageable) {
        return jobRepository.findByClientId(clientId, pageable);
    }

    /**
     * Get jobs by provider with Pagination
     */
    public Page<Job> getJobsByProvider(Long providerId, Pageable pageable) {
        return jobRepository.findByProviderId(providerId, pageable);
    }

    /**
     * Get jobs by province code with Pagination
     */
    public Page<Job> getJobsByProvinceCode(String provinceCode, Pageable pageable) {
        return jobRepository.findJobsByProvinceCode(provinceCode, pageable);
    }

    /**
     * Get open jobs by category and province
     */
    public Page<Job> getOpenJobsByCategoryAndProvince(
            Long categoryId, 
            String provinceCode, 
            Pageable pageable) {
        return jobRepository.findOpenJobsByCategoryAndProvince(categoryId, provinceCode, pageable);
    }

    /**
     * Get all jobs with Pagination
     */
    public Page<Job> getAllJobs(Pageable pageable) {
        return jobRepository.findAll(pageable);
    }

    /**
     * Get job by ID
     */
    public Job getJobById(Long id) {
        return jobRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));
    }
}
