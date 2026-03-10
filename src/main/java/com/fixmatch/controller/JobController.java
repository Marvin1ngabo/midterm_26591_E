package com.fixmatch.controller;

import com.fixmatch.entity.Job;
import com.fixmatch.entity.JobStatus;
import com.fixmatch.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * JobController - REST API endpoints for Job entity
 * 
 * Base URL: /api/jobs
 */
@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "*")
public class JobController {

    @Autowired
    private JobService jobService;

    /**
     * Create new job
     * 
     * POST /api/jobs?clientId=1&categoryId=1&districtId=1
     * Body: { "title": "Fix leaking pipe", "description": "...", "budget": 10000 }
     */
    @PostMapping
    public ResponseEntity<Job> createJob(
            @RequestBody Job job,
            @RequestParam Long clientId,
            @RequestParam Long categoryId,
            @RequestParam Long districtId) {
        try {
            Job created = jobService.createJob(job, clientId, categoryId, districtId);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Assign provider to job
     * 
     * PUT /api/jobs/{jobId}/assign?providerId=2
     */
    @PutMapping("/{jobId}/assign")
    public ResponseEntity<Job> assignProvider(
            @PathVariable Long jobId,
            @RequestParam Long providerId) {
        try {
            Job updated = jobService.assignProvider(jobId, providerId);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Complete job
     * 
     * PUT /api/jobs/{jobId}/complete
     */
    @PutMapping("/{jobId}/complete")
    public ResponseEntity<Job> completeJob(@PathVariable Long jobId) {
        try {
            Job completed = jobService.completeJob(jobId);
            return ResponseEntity.ok(completed);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get job by ID
     * 
     * GET /api/jobs/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        try {
            Job job = jobService.getJobById(id);
            return ResponseEntity.ok(job);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * REQUIREMENT #3: Get all jobs with Pagination and Sorting
     * 
     * GET /api/jobs?page=0&size=10&sortBy=createdAt&direction=desc
     */
    @GetMapping
    public ResponseEntity<Page<Job>> getAllJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        
        Sort sort = direction.equalsIgnoreCase("asc") 
            ? Sort.by(sortBy).ascending() 
            : Sort.by(sortBy).descending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Job> jobs = jobService.getAllJobs(pageable);
        return ResponseEntity.ok(jobs);
    }

    /**
     * Get jobs by status with Pagination
     * 
     * GET /api/jobs/status/OPEN?page=0&size=10
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<Job>> getJobsByStatus(
            @PathVariable JobStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Job> jobs = jobService.getJobsByStatus(status, pageable);
        return ResponseEntity.ok(jobs);
    }

    /**
     * Get jobs by client
     * 
     * GET /api/jobs/client/{clientId}?page=0&size=10
     */
    @GetMapping("/client/{clientId}")
    public ResponseEntity<Page<Job>> getJobsByClient(
            @PathVariable Long clientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Job> jobs = jobService.getJobsByClient(clientId, pageable);
        return ResponseEntity.ok(jobs);
    }

    /**
     * Get jobs by provider
     * 
     * GET /api/jobs/provider/{providerId}?page=0&size=10
     */
    @GetMapping("/provider/{providerId}")
    public ResponseEntity<Page<Job>> getJobsByProvider(
            @PathVariable Long providerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Job> jobs = jobService.getJobsByProvider(providerId, pageable);
        return ResponseEntity.ok(jobs);
    }

    /**
     * Get jobs by province
     * 
     * GET /api/jobs/province/KGL?page=0&size=10
     */
    @GetMapping("/province/{provinceCode}")
    public ResponseEntity<Page<Job>> getJobsByProvince(
            @PathVariable String provinceCode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Job> jobs = jobService.getJobsByProvinceCode(provinceCode, pageable);
        return ResponseEntity.ok(jobs);
    }

    /**
     * Search open jobs by category and province
     * 
     * GET /api/jobs/search?categoryId=1&province=KGL&page=0&size=10
     */
    @GetMapping("/search")
    public ResponseEntity<Page<Job>> searchJobs(
            @RequestParam Long categoryId,
            @RequestParam String province,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Job> jobs = jobService.getOpenJobsByCategoryAndProvince(categoryId, province, pageable);
        return ResponseEntity.ok(jobs);
    }

    /**
     * Get jobs by budget range
     * 
     * GET /api/jobs/budget?min=5000&max=50000&page=0&size=10
     */
    @GetMapping("/budget")
    public ResponseEntity<Page<Job>> getJobsByBudgetRange(
            @RequestParam java.math.BigDecimal min,
            @RequestParam java.math.BigDecimal max,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("budget").descending());
        Page<Job> jobs = jobService.getJobsByBudgetRange(min, max, pageable);
        return ResponseEntity.ok(jobs);
    }

    /**
     * Get available jobs (OPEN status)
     * 
     * GET /api/jobs/available?page=0&size=10
     */
    @GetMapping("/available")
    public ResponseEntity<Page<Job>> getAvailableJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Job> jobs = jobService.getAvailableJobs(pageable);
        return ResponseEntity.ok(jobs);
    }

    /**
     * Search jobs by title
     * 
     * GET /api/jobs/search/title?keyword=plumbing&page=0&size=10
     */
    @GetMapping("/search/title")
    public ResponseEntity<Page<Job>> searchJobsByTitle(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Job> jobs = jobService.searchJobsByTitle(keyword, pageable);
        return ResponseEntity.ok(jobs);
    }

    /**
     * Get job statistics
     * 
     * GET /api/jobs/statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<JobService.JobStatistics> getJobStatistics() {
        JobService.JobStatistics stats = jobService.getJobStatistics();
        return ResponseEntity.ok(stats);
    }

    /**
     * Get recent jobs (last 7 days)
     * 
     * GET /api/jobs/recent
     */
    @GetMapping("/recent")
    public ResponseEntity<java.util.List<Job>> getRecentJobs() {
        java.util.List<Job> jobs = jobService.getRecentJobs();
        return ResponseEntity.ok(jobs);
    }

    /**
     * Get high-budget jobs
     * 
     * GET /api/jobs/high-budget?minBudget=20000&page=0&size=10
     */
    @GetMapping("/high-budget")
    public ResponseEntity<Page<Job>> getHighBudgetJobs(
            @RequestParam(defaultValue = "20000") java.math.BigDecimal minBudget,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Job> jobs = jobService.getHighBudgetJobs(minBudget, pageable);
        return ResponseEntity.ok(jobs);
    }

    /**
     * Cancel job
     * 
     * PUT /api/jobs/{jobId}/cancel
     */
    @PutMapping("/{jobId}/cancel")
    public ResponseEntity<Job> cancelJob(@PathVariable Long jobId) {
        try {
            Job cancelled = jobService.cancelJob(jobId);
            return ResponseEntity.ok(cancelled);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get provider's completed job count
     * 
     * GET /api/jobs/provider/{providerId}/completed-count
     */
    @GetMapping("/provider/{providerId}/completed-count")
    public ResponseEntity<Long> getProviderCompletedJobCount(@PathVariable Long providerId) {
        long count = jobService.getProviderCompletedJobCount(providerId);
        return ResponseEntity.ok(count);
    }

    /**
     * Get client's total job count
     * 
     * GET /api/jobs/client/{clientId}/count
     */
    @GetMapping("/client/{clientId}/count")
    public ResponseEntity<Long> getClientJobCount(@PathVariable Long clientId) {
        long count = jobService.getClientJobCount(clientId);
        return ResponseEntity.ok(count);
    }

    /**
     * Get job statistics by province
     * 
     * GET /api/jobs/statistics/province
     */
    @GetMapping("/statistics/province")
    public ResponseEntity<java.util.List<Object[]>> getJobStatsByProvince() {
        java.util.List<Object[]> stats = jobService.getJobStatsByProvince();
        return ResponseEntity.ok(stats);
    }
}
