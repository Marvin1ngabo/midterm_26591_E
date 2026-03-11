package com.fixmatch.service;

import com.fixmatch.entity.Job;
import com.fixmatch.entity.JobStatus;
import com.fixmatch.entity.User;
import com.fixmatch.entity.ServiceCategory;
import com.fixmatch.entity.Location;
import com.fixmatch.repository.JobRepository;
import com.fixmatch.repository.UserRepository;
import com.fixmatch.repository.ServiceCategoryRepository;
import com.fixmatch.repository.LocationRepository;
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
    private LocationRepository locationRepository;

    /**
     * Create new job
     * 
     * Logic:
     * - Validate client exists
     * - Validate category exists
     * - Validate location exists
     * - Set relationships
     * - Save job
     */
    public Job createJob(Job job, Long clientId, Long categoryId, Long locationId) {
        // Validate client
        User client = userRepository.findById(clientId)
            .orElseThrow(() -> new RuntimeException("Client not found with id: " + clientId));
        
        // Validate category
        ServiceCategory category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
        
        // Validate location
        Location location = locationRepository.findById(locationId)
            .orElseThrow(() -> new RuntimeException("Location not found with id: " + locationId));
        
        // Set relationships
        job.setClient(client);
        job.setCategory(category);
        job.setLocation(location);
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

    /**
     * Get jobs by budget range
     */
    public Page<Job> getJobsByBudgetRange(
            java.math.BigDecimal minBudget, 
            java.math.BigDecimal maxBudget, 
            Pageable pageable) {
        return jobRepository.findJobsByBudgetRange(minBudget, maxBudget, pageable);
    }

    /**
     * Get available jobs (OPEN status)
     */
    public Page<Job> getAvailableJobs(Pageable pageable) {
        return jobRepository.findAvailableJobs(pageable);
    }

    /**
     * Search jobs by title
     */
    public Page<Job> searchJobsByTitle(String keyword, Pageable pageable) {
        return jobRepository.searchJobsByTitle(keyword, pageable);
    }

    /**
     * Get job statistics
     */
    public JobStatistics getJobStatistics() {
        long totalJobs = jobRepository.count();
        long openJobs = jobRepository.countByStatus(JobStatus.OPEN);
        long inProgressJobs = jobRepository.countByStatus(JobStatus.IN_PROGRESS);
        long completedJobs = jobRepository.countByStatus(JobStatus.COMPLETED);
        
        return new JobStatistics(totalJobs, openJobs, inProgressJobs, completedJobs);
    }

    /**
     * Get recent jobs (last 7 days)
     */
    public java.util.List<Job> getRecentJobs() {
        java.time.LocalDateTime sevenDaysAgo = java.time.LocalDateTime.now().minusDays(7);
        return jobRepository.findRecentJobs(sevenDaysAgo);
    }

    /**
     * Get high-budget jobs
     */
    public Page<Job> getHighBudgetJobs(java.math.BigDecimal minBudget, Pageable pageable) {
        return jobRepository.findHighBudgetJobs(minBudget, pageable);
    }

    /**
     * Cancel job (only if OPEN)
     */
    public Job cancelJob(Long jobId) {
        Job job = getJobById(jobId);
        
        if (job.getStatus() != JobStatus.OPEN) {
            throw new IllegalStateException("Only open jobs can be cancelled");
        }
        
        job.setStatus(JobStatus.CANCELLED);
        return jobRepository.save(job);
    }

    /**
     * Get provider's completed job count
     */
    public long getProviderCompletedJobCount(Long providerId) {
        return jobRepository.countByProviderIdAndStatus(providerId, JobStatus.COMPLETED);
    }

    /**
     * Get client's total job count
     */
    public long getClientJobCount(Long clientId) {
        return jobRepository.countByClientId(clientId);
    }

    /**
     * Validate job creation data
     */
    public void validateJobCreation(Job job) {
        if (job.getTitle() == null || job.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Job title is required");
        }
        
        if (job.getDescription() == null || job.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Job description is required");
        }
        
        if (job.getBudget() != null && job.getBudget().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Budget must be positive");
        }
    }

    /**
     * Get jobs by multiple statuses
     */
    public Page<Job> getJobsByStatuses(java.util.List<JobStatus> statuses, Pageable pageable) {
        return jobRepository.findJobsByStatuses(statuses, pageable);
    }

    /**
     * Get job statistics by province
     */
    public java.util.List<Object[]> getJobStatsByProvince() {
        return jobRepository.getJobStatsByProvince();
    }

    /**
     * Inner class for job statistics
     */
    public static class JobStatistics {
        private final long totalJobs;
        private final long openJobs;
        private final long inProgressJobs;
        private final long completedJobs;

        public JobStatistics(long totalJobs, long openJobs, long inProgressJobs, long completedJobs) {
            this.totalJobs = totalJobs;
            this.openJobs = openJobs;
            this.inProgressJobs = inProgressJobs;
            this.completedJobs = completedJobs;
        }

        // Getters
        public long getTotalJobs() { return totalJobs; }
        public long getOpenJobs() { return openJobs; }
        public long getInProgressJobs() { return inProgressJobs; }
        public long getCompletedJobs() { return completedJobs; }
        
        public double getCompletionRate() {
            return totalJobs > 0 ? (double) completedJobs / totalJobs * 100 : 0;
        }
    }
}
