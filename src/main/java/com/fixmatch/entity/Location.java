package com.fixmatch.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Location Entity - Represents complete hierarchical location data
 * 
 * This entity demonstrates:
 * 1. Single table for all location levels
 * 2. Hierarchical data structure
 * 3. Complete Rwanda administrative divisions
 * 
 * Hierarchy: Province → District → Sector → Cell → Village
 */
@Entity
@Table(name = "locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Province level
    @Column(name = "province_code", nullable = false, length = 10)
    private String provinceCode; // e.g., "KGL", "EST", "WST"

    @Column(name = "province_name", nullable = false, length = 100)
    private String provinceName; // e.g., "Kigali", "Eastern Province"

    // District level
    @Column(name = "district_name", nullable = false, length = 100)
    private String districtName; // e.g., "Gasabo", "Kicukiro"

    // Sector level
    @Column(name = "sector_name", length = 100)
    private String sectorName; // e.g., "Kimisagara", "Nyamirambo"

    // Cell level
    @Column(name = "cell_name", length = 100)
    private String cellName; // e.g., "Rugenge", "Nyakabanda"

    // Village level
    @Column(name = "village_name", length = 100)
    private String villageName; // e.g., "Kiyovu", "Kimihurura"

    // Utility constructor for Province and District only
    public Location(String provinceCode, String provinceName, String districtName) {
        this.provinceCode = provinceCode;
        this.provinceName = provinceName;
        this.districtName = districtName;
    }

    // Utility constructor for complete hierarchy
    public Location(String provinceCode, String provinceName, String districtName, 
                   String sectorName, String cellName, String villageName) {
        this.provinceCode = provinceCode;
        this.provinceName = provinceName;
        this.districtName = districtName;
        this.sectorName = sectorName;
        this.cellName = cellName;
        this.villageName = villageName;
    }

    /**
     * Get formatted full address
     */
    public String getFullAddress() {
        StringBuilder address = new StringBuilder();
        
        if (villageName != null) address.append(villageName).append(", ");
        if (cellName != null) address.append(cellName).append(", ");
        if (sectorName != null) address.append(sectorName).append(", ");
        address.append(districtName).append(", ");
        address.append(provinceName);
        
        return address.toString();
    }

    /**
     * Get short address (District, Province)
     */
    public String getShortAddress() {
        return districtName + ", " + provinceName;
    }

    /**
     * Check if location has complete hierarchy
     */
    public boolean isCompleteHierarchy() {
        return provinceCode != null && provinceName != null && 
               districtName != null && sectorName != null && 
               cellName != null && villageName != null;
    }

    /**
     * Get location level (how detailed the location is)
     */
    public String getLocationLevel() {
        if (villageName != null) return "Village";
        if (cellName != null) return "Cell";
        if (sectorName != null) return "Sector";
        return "District";
    }
}