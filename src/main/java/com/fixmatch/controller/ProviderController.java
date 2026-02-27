package com.fixmatch.controller;

import com.fixmatch.entity.ProviderProfile;
import com.fixmatch.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

/**
 * ProviderController - REST API endpoints for ProviderProfile entity
 * 
 * Base URL: /api/providers
 */
@RestController
@RequestMapping("/api/providers")
@CrossOrigin(origins = "*")
public class ProviderController {

    @Autowired
    private ProviderService providerService;

    /**
     * Create provider profile
     * 
     * POST /api/providers?userId=1
     * Body: { "bio": "Experienced plumber", "hourlyRate": 5000, "yearsExperience": 5 }
     */
    @PostMapping
    public ResponseEntity<ProviderProfile> createProviderProfile(
            @RequestParam Long userId,
            @RequestBody ProviderProfile profile) {
        try {
            ProviderProfile created = providerService.createProviderProfile(userId, profile);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * REQUIREMENT #4: Add skill to provider (Many-to-Many)
     * 
     * POST /api/providers/{providerId}/skills?skillName=Plumbing
     */
    @PostMapping("/{providerId}/skills")
    public ResponseEntity<ProviderProfile> addSkill(
            @PathVariable Long providerId,
            @RequestParam String skillName) {
        try {
            ProviderProfile updated = providerService.addSkillToProvider(providerId, skillName);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get provider by user ID
     * 
     * GET /api/providers/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ProviderProfile> getProviderByUserId(@PathVariable Long userId) {
        try {
            ProviderProfile provider = providerService.getProviderByUserId(userId);
            return ResponseEntity.ok(provider);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * REQUIREMENT #3: Get all providers with Pagination and Sorting
     * 
     * GET /api/providers?page=0&size=10&sortBy=rating&direction=desc
     */
    @GetMapping
    public ResponseEntity<Page<ProviderProfile>> getAllProviders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "rating") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        
        Sort sort = direction.equalsIgnoreCase("asc") 
            ? Sort.by(sortBy).ascending() 
            : Sort.by(sortBy).descending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProviderProfile> providers = providerService.getAllProviders(pageable);
        return ResponseEntity.ok(providers);
    }

    /**
     * Get verified providers with Pagination
     * 
     * GET /api/providers/verified?page=0&size=10
     */
    @GetMapping("/verified")
    public ResponseEntity<Page<ProviderProfile>> getVerifiedProviders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("rating").descending());
        Page<ProviderProfile> providers = providerService.getVerifiedProviders(pageable);
        return ResponseEntity.ok(providers);
    }

    /**
     * REQUIREMENT #4: Get providers by skill (Many-to-Many query)
     * 
     * GET /api/providers/skill/Plumbing?page=0&size=10
     */
    @GetMapping("/skill/{skillName}")
    public ResponseEntity<Page<ProviderProfile>> getProvidersBySkill(
            @PathVariable String skillName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<ProviderProfile> providers = providerService.getProvidersBySkill(skillName, pageable);
        return ResponseEntity.ok(providers);
    }

    /**
     * Get providers by skill and province (Complex query)
     * 
     * GET /api/providers/search?skill=Plumbing&province=KGL&minRating=4.0&page=0&size=10
     */
    @GetMapping("/search")
    public ResponseEntity<Page<ProviderProfile>> searchProviders(
            @RequestParam String skill,
            @RequestParam String province,
            @RequestParam(defaultValue = "0.0") BigDecimal minRating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("rating").descending());
        Page<ProviderProfile> providers = providerService.getProvidersBySkillAndProvince(
            skill, province, minRating, pageable
        );
        return ResponseEntity.ok(providers);
    }

    /**
     * Update provider profile
     * 
     * PUT /api/providers/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProviderProfile> updateProvider(
            @PathVariable Long id,
            @RequestBody ProviderProfile profile) {
        try {
            ProviderProfile updated = providerService.updateProvider(id, profile);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
