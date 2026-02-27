package com.fixmatch.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * ProviderProfile Entity - Extended profile for service providers
 * 
 * Relationships:
 * - One-to-One with User (One provider profile belongs to one user)
 * - Many-to-Many with Skill (One provider has many skills, one skill belongs to many providers)
 * 
 * This entity demonstrates:
 * 1. One-to-One relationship
 * 2. Many-to-Many relationship
 * 3. Join table usage
 */
@Entity
@Table(name = "provider_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"user", "skills"})
@ToString(exclude = {"user", "skills"})
public class ProviderProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * One-to-One Relationship with User
     * - One provider profile belongs to one user
     * - @JoinColumn: This side owns the relationship (has the foreign key)
     * - unique = true: Ensures one user can only have one provider profile
     */
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonIgnore
    private User user;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "hourly_rate", precision = 10, scale = 2)
    private BigDecimal hourlyRate;

    @Column(name = "years_experience")
    private Integer yearsExperience;

    @Column(name = "verification_status")
    private Boolean verificationStatus = false;

    @Column(precision = 3, scale = 2)
    private BigDecimal rating = BigDecimal.ZERO;

    @Column(name = "total_jobs_completed")
    private Integer totalJobsCompleted = 0;

    /**
     * Many-to-Many Relationship with Skill
     * - One provider can have many skills
     * - One skill can belong to many providers
     * - @JoinTable: Defines the join table
     *   - name: Name of the join table
     *   - joinColumns: Foreign key to this entity (ProviderProfile)
     *   - inverseJoinColumns: Foreign key to the other entity (Skill)
     */
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "provider_skills",
        joinColumns = @JoinColumn(name = "provider_id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skill> skills = new HashSet<>();

    // Helper method to add skill
    public void addSkill(Skill skill) {
        this.skills.add(skill);
        skill.getProviders().add(this);
    }

    // Helper method to remove skill
    public void removeSkill(Skill skill) {
        this.skills.remove(skill);
        skill.getProviders().remove(this);
    }
}
