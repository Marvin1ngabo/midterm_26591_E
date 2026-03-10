package com.fixmatch.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * User Entity - Represents both clients and service providers
 * 
 * Relationships:
 * - Many-to-One with District (Many users belong to one district)
 * - One-to-One with ProviderProfile (Optional, only for providers)
 * 
 * This entity demonstrates:
 * 1. Basic entity with validation
 * 2. Enum usage for user types
 * 3. Email and phone uniqueness
 * 4. Timestamp tracking
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false, length = 100)
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    private String password;

    @Column(unique = true, length = 20)
    private String phone;

    /**
     * UserType Enum - Determines if user is CLIENT, PROVIDER, or BOTH
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType userType;

    /**
     * Many-to-One Relationship with District
     * - Many users can belong to one district
     */
    @ManyToOne
    @JoinColumn(name = "district_id")
    private District district;

    /**
     * One-to-One Relationship with ProviderProfile
     * - Only providers have a profile
     * - mappedBy: Indicates ProviderProfile owns the relationship
     * - cascade: Operations on User cascade to ProviderProfile
     */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProviderProfile providerProfile;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Automatically set timestamps before persisting
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Automatically update timestamp before updating
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Utility method to check if user is a provider
     */
    public boolean isProvider() {
        return userType == UserType.PROVIDER || userType == UserType.BOTH;
    }

    /**
     * Utility method to check if user is a client
     */
    public boolean isClient() {
        return userType == UserType.CLIENT || userType == UserType.BOTH;
    }

    /**
     * Get full location string (District, Province)
     */
    public String getFullLocation() {
        if (district != null && district.getProvince() != null) {
            return district.getName() + ", " + district.getProvince().getName();
        }
        return district != null ? district.getName() : "Location not set";
    }

    /**
     * Check if user has a complete profile
     */
    public boolean hasCompleteProfile() {
        return name != null && !name.trim().isEmpty() &&
               email != null && !email.trim().isEmpty() &&
               phone != null && !phone.trim().isEmpty() &&
               district != null;
    }
}
