package com.fixmatch.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Set;

/**
 * Skill Entity - Represents skills that providers can have
 * 
 * Relationships:
 * - Many-to-Many with ProviderProfile (Many skills belong to many providers)
 * 
 * This entity demonstrates:
 * 1. The inverse side of Many-to-Many relationship
 * 2. Using mappedBy to indicate the owning side
 * 
 * Examples: "Pipe Repair", "House Cleaning", "Electrical Wiring", "Wall Painting"
 */
@Entity
@Table(name = "skills")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "providers")
@ToString(exclude = "providers")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * Many-to-Many Relationship with ProviderProfile (Inverse side)
     * - mappedBy: Indicates that ProviderProfile owns the relationship
     * - @JsonIgnore: Prevents infinite recursion when serializing to JSON
     */
    @ManyToMany(mappedBy = "skills")
    @JsonIgnore
    private Set<ProviderProfile> providers = new HashSet<>();

    // Constructor without relationships
    public Skill(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
