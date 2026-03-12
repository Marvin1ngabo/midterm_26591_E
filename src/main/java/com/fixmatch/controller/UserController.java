package com.fixmatch.controller;

import com.fixmatch.entity.User;
import com.fixmatch.entity.UserType;
import com.fixmatch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * UserController - REST API endpoints for User entity
 * 
 * Base URL: /api/users
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Register new user with location
     * 
     * POST /api/users/register?locationId=1
     * Body: { "name": "John", "email": "john@example.com", "password": "pass", "userType": "CLIENT" }
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(
            @RequestBody User user,
            @RequestParam(required = false) Long locationId) {
        try {
            User registered = userService.registerUser(user, locationId);
            return new ResponseEntity<>(registered, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Register new user by village name (easier for frontend)
     * 
     * POST /api/users/register/village?villageName=Kiyovu
     * Body: { "name": "John", "email": "john@example.com", "password": "pass", "userType": "CLIENT" }
     */
    @PostMapping("/register/village")
    public ResponseEntity<User> registerUserByVillage(
            @RequestBody User user,
            @RequestParam String villageName) {
        try {
            User registered = userService.registerUserByVillage(user, villageName);
            return new ResponseEntity<>(registered, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get user by ID
     * 
     * GET /api/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * REQUIREMENT #3: Get all users with Sorting
     * 
     * GET /api/users?sortBy=name&direction=asc
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        
        Sort sort = direction.equalsIgnoreCase("asc") 
            ? Sort.by(sortBy).ascending() 
            : Sort.by(sortBy).descending();
        
        List<User> users = userService.getAllUsers(sort);
        return ResponseEntity.ok(users);
    }

    /**
     * REQUIREMENT #3: Get users by type with Pagination and Sorting
     * 
     * GET /api/users/type/PROVIDER?page=0&size=10&sortBy=name&direction=asc
     */
    @GetMapping("/type/{userType}")
    public ResponseEntity<Page<User>> getUsersByType(
            @PathVariable UserType userType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        
        Sort sort = direction.equalsIgnoreCase("asc") 
            ? Sort.by(sortBy).ascending() 
            : Sort.by(sortBy).descending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<User> users = userService.getUsersByType(userType, pageable);
        return ResponseEntity.ok(users);
    }

    /**
     * REQUIREMENT #8: Get users by province CODE
     * 
     * GET /api/users/province/code/KGL
     */
    @GetMapping("/province/code/{code}")
    public ResponseEntity<List<User>> getUsersByProvinceCode(@PathVariable String code) {
        List<User> users = userService.getUsersByProvinceCode(code);
        return ResponseEntity.ok(users);
    }

    /**
     * REQUIREMENT #8: Get users by province NAME
     * 
     * GET /api/users/province/name/Kigali
     */
    @GetMapping("/province/name/{name}")
    public ResponseEntity<List<User>> getUsersByProvinceName(@PathVariable String name) {
        List<User> users = userService.getUsersByProvinceName(name);
        return ResponseEntity.ok(users);
    }

    /**
     * Get users by district name
     * 
     * GET /api/users/district/name/Gasabo
     */
    @GetMapping("/district/name/{name}")
    public ResponseEntity<List<User>> getUsersByDistrictName(@PathVariable String name) {
        List<User> users = userService.getUsersByDistrictName(name);
        return ResponseEntity.ok(users);
    }

    /**
     * Get users by sector name
     * 
     * GET /api/users/sector/name/Kimisagara
     */
    @GetMapping("/sector/name/{name}")
    public ResponseEntity<List<User>> getUsersBySectorName(@PathVariable String name) {
        List<User> users = userService.getUsersBySectorName(name);
        return ResponseEntity.ok(users);
    }

    /**
     * Get users by cell name
     * 
     * GET /api/users/cell/name/Rugenge
     */
    @GetMapping("/cell/name/{name}")
    public ResponseEntity<List<User>> getUsersByCellName(@PathVariable String name) {
        List<User> users = userService.getUsersByCellName(name);
        return ResponseEntity.ok(users);
    }

    /**
     * Get users by village name
     * 
     * GET /api/users/village/name/Kiyovu
     */
    @GetMapping("/village/name/{name}")
    public ResponseEntity<List<User>> getUsersByVillageName(@PathVariable String name) {
        List<User> users = userService.getUsersByVillageName(name);
        return ResponseEntity.ok(users);
    }

    /**
     * Get users by location hierarchy
     * 
     * GET /api/users/location?provinceCode=KGL&districtName=Gasabo&sectorName=Kimisagara&cellName=Rugenge&villageName=Kiyovu
     */
    @GetMapping("/location")
    public ResponseEntity<List<User>> getUsersByLocationHierarchy(
            @RequestParam String provinceCode,
            @RequestParam String districtName,
            @RequestParam(required = false) String sectorName,
            @RequestParam(required = false) String cellName,
            @RequestParam(required = false) String villageName) {
        
        List<User> users = userService.getUsersByLocationHierarchy(
            provinceCode, districtName, sectorName, cellName, villageName
        );
        return ResponseEntity.ok(users);
    }

    /**
     * Get providers by province with Pagination
     * 
     * GET /api/users/providers/province/KGL?page=0&size=10
     */
    @GetMapping("/providers/province/{code}")
    public ResponseEntity<Page<User>> getProvidersByProvince(
            @PathVariable String code,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<User> providers = userService.getProvidersByProvinceCode(code, pageable);
        return ResponseEntity.ok(providers);
    }

    /**
     * REQUIREMENT #7: Check if email exists
     * 
     * GET /api/users/exists/email?email=john@example.com
     */
    @GetMapping("/exists/email")
    public ResponseEntity<Boolean> emailExists(@RequestParam String email) {
        boolean exists = userService.userExistsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    /**
     * REQUIREMENT #7: Check if phone exists
     * 
     * GET /api/users/exists/phone?phone=0781234567
     */
    @GetMapping("/exists/phone")
    public ResponseEntity<Boolean> phoneExists(@RequestParam String phone) {
        boolean exists = userService.userExistsByPhone(phone);
        return ResponseEntity.ok(exists);
    }

    /**
     * Update user
     * 
     * PUT /api/users/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            User updated = userService.updateUser(id, user);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete user
     * 
     * DELETE /api/users/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get verified providers with pagination
     * 
     * GET /api/users/providers/verified?page=0&size=10
     */
    @GetMapping("/providers/verified")
    public ResponseEntity<Page<User>> getVerifiedProviders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "rating") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        
        Sort sort = direction.equalsIgnoreCase("asc") 
            ? Sort.by(sortBy).ascending() 
            : Sort.by(sortBy).descending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<User> providers = userService.getVerifiedProviders(pageable);
        return ResponseEntity.ok(providers);
    }

    /**
     * Get providers by skill
     * 
     * GET /api/users/providers/skill/Plumbing
     */
    @GetMapping("/providers/skill/{skillName}")
    public ResponseEntity<List<User>> getProvidersBySkill(@PathVariable String skillName) {
        List<User> providers = userService.getProvidersBySkill(skillName);
        return ResponseEntity.ok(providers);
    }

    /**
     * Get top-rated providers
     * 
     * GET /api/users/providers/top-rated?minRating=4.0
     */
    @GetMapping("/providers/top-rated")
    public ResponseEntity<List<User>> getTopRatedProviders(
            @RequestParam(defaultValue = "4.0") Double minRating) {
        List<User> providers = userService.getTopRatedProviders(minRating);
        return ResponseEntity.ok(providers);
    }

    /**
     * Get user statistics
     * 
     * GET /api/users/statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<UserService.UserStatistics> getUserStatistics() {
        UserService.UserStatistics stats = userService.getUserStatistics();
        return ResponseEntity.ok(stats);
    }

    /**
     * Get recent users (last 30 days)
     * 
     * GET /api/users/recent
     */
    @GetMapping("/recent")
    public ResponseEntity<List<User>> getRecentUsers() {
        List<User> users = userService.getRecentUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Get experienced providers
     * 
     * GET /api/users/providers/experienced?minExperience=5
     */
    @GetMapping("/providers/experienced")
    public ResponseEntity<List<User>> getExperiencedProviders(
            @RequestParam(defaultValue = "5") Integer minExperience) {
        List<User> providers = userService.getExperiencedProviders(minExperience);
        return ResponseEntity.ok(providers);
    }

    /**
     * Search users by name
     * 
     * GET /api/users/search?name=John
     */
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsersByName(@RequestParam String name) {
        List<User> users = userService.searchUsersByName(name);
        return ResponseEntity.ok(users);
    }

    /**
     * Get user by email
     * 
     * GET /api/users/email/{email}
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        try {
            User user = userService.getUserByEmail(email);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
