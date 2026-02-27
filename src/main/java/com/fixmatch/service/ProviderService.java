package com.fixmatch.service;

import com.fixmatch.entity.ProviderProfile;
import com.fixmatch.entity.Skill;
import com.fixmatch.entity.User;
import com.fixmatch.entity.UserType;
import com.fixmatch.repository.ProviderProfileRepository;
import com.fixmatch.repository.SkillRepository;
import com.fixmatch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

/**
 * ProviderService - Business logic for ProviderProfile entity
 * 
 * Demonstrates:
 * 1. One-to-One relationship (User → ProviderProfile)
 * 2. Many-to-Many relationship (ProviderProfile ↔ Skill)
 * 3. Complex queries with pagination
 */
@Service
@Transactional
public class ProviderService {

    @Autowired
    private ProviderProfileRepository providerProfileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SkillRepository skillRepository;

    /**
     * Create provider profile
     * 
     * Logic:
     * - Verify user exists and is a PROVIDER
     * - Create One-to-One relationship with User
     * - Save provider profile
     */
    public ProviderProfile createProviderProfile(Long userId, ProviderProfile profile) {
        // Get user
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        // Verify user is a provider
        if (user.getUserType() != UserType.PROVIDER && user.getUserType() != UserType.BOTH) {
            throw new RuntimeException("User must be a PROVIDER to create a profile");
        }
        
        // Check if profile already exists
        if (providerProfileRepository.findByUserId(userId).isPresent()) {
            throw new RuntimeException("Provider profile already exists for this user");
        }
        
        // Set One-to-One relationship
        profile.setUser(user);
        
        return providerProfileRepository.save(profile);
    }

    /**
     * Add skill to provider (Many-to-Many relationship)
     * 
     * Logic:
     * - Find provider profile
     * - Find or create skill
     * - Add skill to provider's skill set
     * - Save (join table entry is automatically created)
     */
    public ProviderProfile addSkillToProvider(Long providerId, String skillName) {
        // Get provider profile
        ProviderProfile provider = providerProfileRepository.findById(providerId)
            .orElseThrow(() -> new RuntimeException("Provider not found with id: " + providerId));
        
        // Find or create skill
        Skill skill = skillRepository.findByName(skillName)
            .orElseGet(() -> {
                Skill newSkill = new Skill(skillName, "");
                return skillRepository.save(newSkill);
            });
        
        // Add skill (Many-to-Many relationship)
        provider.addSkill(skill);
        
        return providerProfileRepository.save(provider);
    }

    /**
     * Get provider profile by user ID
     */
    public ProviderProfile getProviderByUserId(Long userId) {
        return providerProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("Provider profile not found for user id: " + userId));
    }

    /**
     * Get all verified providers with Pagination and Sorting
     * 
     * Example: Sort by rating descending
     */
    public Page<ProviderProfile> getVerifiedProviders(Pageable pageable) {
        return providerProfileRepository.findByVerificationStatus(true, pageable);
    }

    /**
     * Get providers by skill with Pagination
     * 
     * Demonstrates: Querying Many-to-Many relationship
     */
    public Page<ProviderProfile> getProvidersBySkill(String skillName, Pageable pageable) {
        return providerProfileRepository.findBySkillName(skillName, pageable);
    }

    /**
     * Get providers by skill and province with Pagination
     * 
     * Complex query demonstrating:
     * - Many-to-Many relationship (Provider ↔ Skill)
     * - Multiple joins (Provider → User → District → Province)
     * - Filtering by multiple criteria
     */
    public Page<ProviderProfile> getProvidersBySkillAndProvince(
            String skillName, 
            String provinceCode, 
            BigDecimal minRating, 
            Pageable pageable) {
        return providerProfileRepository.findProvidersBySkillAndProvinceAndRating(
            skillName, provinceCode, minRating, pageable
        );
    }

    /**
     * Update provider profile
     */
    public ProviderProfile updateProvider(Long id, ProviderProfile updatedProfile) {
        ProviderProfile existing = providerProfileRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Provider not found with id: " + id));
        
        existing.setBio(updatedProfile.getBio());
        existing.setHourlyRate(updatedProfile.getHourlyRate());
        existing.setYearsExperience(updatedProfile.getYearsExperience());
        
        return providerProfileRepository.save(existing);
    }

    /**
     * Get all providers with Pagination
     */
    public Page<ProviderProfile> getAllProviders(Pageable pageable) {
        return providerProfileRepository.findAll(pageable);
    }
}
