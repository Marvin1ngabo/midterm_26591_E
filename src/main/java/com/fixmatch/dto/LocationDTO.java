package com.fixmatch.dto;

import com.fixmatch.entity.LocationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LocationDTO - Data Transfer Object for Location
 * 
 * This DTO prevents JSON serialization issues with circular references
 * in self-referencing relationships while providing all necessary data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDTO {
    
    private Long locationId;
    private String name;
    private String code;
    private LocationType type;
    private String fullPath;
    private String formattedAddress;
    private int depthLevel;
    private boolean isRoot;
    private boolean isLeaf;
    private Long parentLocationId;
    private String parentLocationName;
    
    /**
     * Constructor for basic location info
     */
    public LocationDTO(Long locationId, String name, String code, LocationType type) {
        this.locationId = locationId;
        this.name = name;
        this.code = code;
        this.type = type;
    }
    
    /**
     * Constructor with parent info
     */
    public LocationDTO(Long locationId, String name, String code, LocationType type, 
                                 Long parentLocationId, String parentLocationName) {
        this.locationId = locationId;
        this.name = name;
        this.code = code;
        this.type = type;
        this.parentLocationId = parentLocationId;
        this.parentLocationName = parentLocationName;
    }
}