package com.fixmatch.service;

import com.fixmatch.entity.User;
import com.fixmatch.entity.UserType;
import com.fixmatch.entity.Location;
import com.fixmatch.repository.UserRepository;
import com.fixmatch.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * UserService - Business logic for User entity
 * 
 * Demonstrates:
 * 1. existsBy() methods (Requirement #7)
 * 2. Pagination and Sorting (Requirement #3)
 * 3. Retrieve users by province (Requirement #8)
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;

    /**
     * Register new user with location
     * 
     * Logic:
     * - Check if email already exists using existsByEmail()
     * - Check if phone already exists using existsByPhone()
     * - Set location if provided
     * - Save user if validation passes
     */
    public User registerUser(User user, Long locationId) {
        // REQUIREMENT #7: Use existsBy() methods
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists: " + user.getEmail());
        }
        
        if (user.getPhone() != null && userRepository.existsByPhone(user.getPhone())) {
            throw new RuntimeException("Phone number already exists: " + user.getPhone());
        }
        
        // Set location if provided
        if (locationId != null) {
            Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + locationId));
            user.setLocation(location);
        }
        
        // In real app, hash password here
        // user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        return userRepository.save(user);
    }

    /**
     * Register new user by village name
     * 
     * This method finds the location by village name and automatically
     * links the user to the complete location hierarchy
     */
    public User registerUserByVillage(User user, String villageName) {
        // REQUIREMENT #7: Use existsBy() methods
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists: " + user.getEmail());
        }
        
        if (user.getPhone() != null && userRepository.existsByPhone(user.getPhone())) {
            throw new RuntimeException("Phone number already exists: " + user.getPhone());
        }
        
        // Find location by village name
        Location location = locationRepository.findByVillageName(villageName)
            .orElseThrow(() -> new RuntimeException("Village not found: " + villageName));
        
        // Set the complete location (which includes Province, District, Sector, Cell, Village)
        user.setLocation(location);
        
        return userRepository.save(user);
    }

    /**
     * Register new user (backward compatibility)
     */
    public User registerUser(User user) {
        return registerUser(user, null);
    }

    /**
     * Get user by ID
     */
    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    /**
     * Get user by email
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    /**
     * REQUIREMENT #3: Get all users with Sorting
     * 
     * Example: Sort by name ascending
     * Sort sort = Sort.by("name").ascending();
     */
    public List<User> getAllUsers(Sort sort) {
        return userRepository.findAll(sort);
    }

    /**
     * REQUIREMENT #3: Get users by type with Pagination and Sorting
     * 
     * Example: Get providers, page 0, size 10, sorted by name
     * Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
     * 
     * Benefits:
     * - Only loads 10 users at a time (reduces memory)
     * - Improves query performance (LIMIT in SQL)
     * - Better user experience (infinite scroll)
     */
    public Page<User> getUsersByType(UserType userType, Pageable pageable) {
        return userRepository.findByUserType(userType, pageable);
    }

    /**
     * REQUIREMENT #8: Get all users from a province by CODE
     * 
     * Logic:
     * - Uses JPQL query with joins: User → District → Province
     * - Filters by province code
     */
    public List<User> getUsersByProvinceCode(String provinceCode) {
        return userRepository.findUsersByProvinceCode(provinceCode);
    }

    /**
     * REQUIREMENT #8: Get all users from a province by NAME
     * 
     * Logic:
     * - Uses JPQL query with joins: User → District → Province
     * - Filters by province name
     */
    public List<User> getUsersByProvinceName(String provinceName) {
        return userRepository.findUsersByProvinceName(provinceName);
    }

    /**
     * Get users by district name
     */
    public List<User> getUsersByDistrictName(String districtName) {
        return userRepository.findUsersByDistrictName(districtName);
    }

    /**
     * Get users by sector name
     */
    public List<User> getUsersBySectorName(String sectorName) {
        return userRepository.findUsersBySectorName(sectorName);
    }

    /**
     * Get users by cell name
     */
    public List<User> getUsersByCellName(String cellName) {
        return userRepository.findUsersByCellName(cellName);
    }

    /**
     * Get users by village name
     */
    public List<User> getUsersByVillageName(String villageName) {
        return userRepository.findUsersByVillageName(villageName);
    }

    /**
     * Get users by complete location hierarchy
     * 
     * This allows querying by any combination of location levels:
     * - Province + District (required)
     * - Province + District + Sector (optional)
     * - Province + District + Sector + Cell (optional)
     * - Province + District + Sector + Cell + Village (optional)
     */
    public List<User> getUsersByLocationHierarchy(String provinceCode, String districtName, 
                                                 String sectorName, String cellName, String villageName) {
        return userRepository.findUsersByLocationHierarchy(provinceCode, districtName, 
                                                          sectorName, cellName, villageName);
    }

    /**
     * Get providers by province with Pagination
     * 
     * Combines:
     * - Province filtering
     * - User type filtering (only PROVIDER and BOTH)
     * - Pagination
     */
    public Page<User> getProvidersByProvinceCode(String provinceCode, Pageable pageable) {
        return userRepository.findProvidersByProvinceCode(provinceCode, pageable);
    }

    /**
     * REQUIREMENT #7: Check if user exists by email
     */
    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * REQUIREMENT #7: Check if user exists by phone
     */
    public boolean userExistsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    /**
     * Update user
     */
    public User updateUser(Long id, User updatedUser) {
        User existingUser = getUserById(id);
        
        // Update fields
        existingUser.setName(updatedUser.getName());
        existingUser.setPhone(updatedUser.getPhone());
        existingUser.setLocation(updatedUser.getLocation());
        
        return userRepository.save(existingUser);
    }

    /**
     * Delete user
     */
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    /**
     * Get verified providers with pagination
     */
    public Page<User> getVerifiedProviders(Pageable pageable) {
        return userRepository.findVerifiedProviders(pageable);
    }

    /**
     * Find providers by skill
     */
    public List<User> getProvidersBySkill(String skillName) {
        return userRepository.findProvidersBySkill(skillName);
    }

    /**
     * Get top-rated providers
     */
    public List<User> getTopRatedProviders(Double minRating) {
        return userRepository.findTopRatedProviders(minRating);
    }

    /**
     * Get user statistics
     */
    public UserStatistics getUserStatistics() {
        long totalUsers = userRepository.count();
        long totalClients = userRepository.countByUserType(UserType.CLIENT);
        long totalProviders = userRepository.countByUserType(UserType.PROVIDER);
        long totalBoth = userRepository.countByUserType(UserType.BOTH);
        
        return new UserStatistics(totalUsers, totalClients, totalProviders, totalBoth);
    }

    /**
     * Get recent users (last 30 days)
     */
    public List<User> getRecentUsers() {
        java.time.LocalDateTime thirtyDaysAgo = java.time.LocalDateTime.now().minusDays(30);
        return userRepository.findRecentUsers(thirtyDaysAgo);
    }

    /**
     * Find experienced providers
     */
    public List<User> getExperiencedProviders(Integer minExperience) {
        return userRepository.findExperiencedProviders(minExperience);
    }

    /**
     * Search users by name
     */
    public List<User> searchUsersByName(String name) {
        return userRepository.searchByName(name);
    }

    /**
     * Validate user registration data
     */
    public void validateUserRegistration(User user) {
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        
        if (user.getPassword() == null || user.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
        
        if (user.getUserType() == null) {
            throw new IllegalArgumentException("User type is required");
        }
    }

    /**
     * Inner class for user statistics
     */
    public static class UserStatistics {
        private final long totalUsers;
        private final long totalClients;
        private final long totalProviders;
        private final long totalBoth;

        public UserStatistics(long totalUsers, long totalClients, long totalProviders, long totalBoth) {
            this.totalUsers = totalUsers;
            this.totalClients = totalClients;
            this.totalProviders = totalProviders;
            this.totalBoth = totalBoth;
        }

        // Getters
        public long getTotalUsers() { return totalUsers; }
        public long getTotalClients() { return totalClients; }
        public long getTotalProviders() { return totalProviders; }
        public long getTotalBoth() { return totalBoth; }
    }
}
