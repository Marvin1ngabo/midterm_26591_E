package com.fixmatch.service;

import com.fixmatch.entity.User;
import com.fixmatch.entity.UserType;
import com.fixmatch.repository.UserRepository;
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

    /**
     * Register new user
     * 
     * Logic:
     * - Check if email already exists using existsByEmail()
     * - Check if phone already exists using existsByPhone()
     * - Save user if validation passes
     */
    public User registerUser(User user) {
        // REQUIREMENT #7: Use existsBy() methods
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists: " + user.getEmail());
        }
        
        if (user.getPhone() != null && userRepository.existsByPhone(user.getPhone())) {
            throw new RuntimeException("Phone number already exists: " + user.getPhone());
        }
        
        // In real app, hash password here
        // user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        return userRepository.save(user);
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
        existingUser.setDistrict(updatedUser.getDistrict());
        
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
}
