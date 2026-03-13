package com.fixmatch.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.ArrayList;

/**
 * Location Entity - Represents hierarchical location data using Adjacency List Model
 * 
 * This entity demonstrates:
 * 1. Self-referencing relationship (parent-child)
 * 2. Tree data structure for geographical hierarchy
 * 3. Adjacency List Model implementation
 * 
 * Hierarchy: Province → District → Sector → Cell → Village
 */
@Entity
@Table(name = "locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long locationId;

    @Column(name = "name", nullable = false, length = 100)
    private String name; // e.g., "Kigali City", "Gasabo", "Kimironko"

    @Column(name = "code", length = 10)
    private String code; // e.g., "KGL", "GAS", "KIM"

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private LocationType type; // PROVINCE, DISTRICT, SECTOR, CELL, VILLAGE

    /**
     * Self-referencing relationship - Parent Location
     * Many locations can belong to one parent location
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_location_id")
    @JsonIgnore // Prevent infinite recursion in JSON serialization
    private Location parentLocation;

    /**
     * Self-referencing relationship - Child Locations
     * One location can have many child locations
     */
    @OneToMany(mappedBy = "parentLocation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore // Prevent infinite recursion in JSON serialization
    private List<Location> childLocations = new ArrayList<>();

    // Constructors for different scenarios
    public Location(String name, String code, LocationType type) {
        this.name = name;
        this.code = code;
        this.type = type;
    }

    public Location(String name, String code, LocationType type, Location parent) {
        this.name = name;
        this.code = code;
        this.type = type;
        this.parentLocation = parent;
    }

    /**
     * Get full location path from root to current location
     * Example: "Kigali City → Gasabo → Kimironko → Bibare → Nyagatovu"
     */
    public String getFullPath() {
        List<String> path = new ArrayList<>();
        Location current = this;
        
        // Build path from current location up to root
        while (current != null) {
            path.add(0, current.getName()); // Add at beginning to reverse order
            current = current.getParentLocation();
        }
        
        return String.join(" → ", path);
    }

    /**
     * Get all ancestors (parent, grandparent, etc.) up to root
     */
    public List<Location> getAncestors() {
        List<Location> ancestors = new ArrayList<>();
        Location current = this.parentLocation;
        
        while (current != null) {
            ancestors.add(current);
            current = current.getParentLocation();
        }
        
        return ancestors;
    }

    /**
     * Get root location (Province level)
     */
    public Location getRoot() {
        Location current = this;
        while (current.getParentLocation() != null) {
            current = current.getParentLocation();
        }
        return current;
    }

    /**
     * Check if this location is a root (has no parent)
     */
    public boolean isRoot() {
        return parentLocation == null;
    }

    /**
     * Check if this location is a leaf (has no children)
     */
    public boolean isLeaf() {
        return childLocations.isEmpty();
    }

    /**
     * Get depth level in hierarchy (0 = root/province, 4 = village)
     */
    public int getDepthLevel() {
        int depth = 0;
        Location current = this;
        while (current.getParentLocation() != null) {
            depth++;
            current = current.getParentLocation();
        }
        return depth;
    }

    /**
     * Add child location
     */
    public void addChild(Location child) {
        childLocations.add(child);
        child.setParentLocation(this);
    }

    /**
     * Remove child location
     */
    public void removeChild(Location child) {
        childLocations.remove(child);
        child.setParentLocation(null);
    }

    /**
     * Get location at specific type in the hierarchy
     * Example: getLocationByType(LocationType.PROVINCE) returns the province
     */
    public Location getLocationByType(LocationType targetType) {
        Location current = this;
        
        // Go up the hierarchy to find the target type
        while (current != null) {
            if (current.getType() == targetType) {
                return current;
            }
            current = current.getParentLocation();
        }
        
        return null; // Type not found in hierarchy
    }

    /**
     * Get formatted address string
     */
    public String getFormattedAddress() {
        List<String> addressParts = new ArrayList<>();
        Location current = this;
        
        // Build address from current location up to province
        while (current != null) {
            addressParts.add(0, current.getName());
            current = current.getParentLocation();
        }
        
        return String.join(", ", addressParts);
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - %s", name, type, code != null ? code : "No Code");
    }
}