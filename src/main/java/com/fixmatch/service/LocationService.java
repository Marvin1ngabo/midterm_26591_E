package com.fixmatch.service;

import com.fixmatch.entity.Location;
import com.fixmatch.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * LocationService - Business logic for Location entity
 * 
 * Demonstrates:
 * 1. existsBy() methods (Requirement #7)
 * 2. Hierarchical location management
 * 3. Pagination and Sorting (Requirement #3)
 */
@Service
@Transactional
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    /**
     * Save location
     */
    public Location saveLocation(Location location) {
        // Validate required fields
        if (location.getProvinceCode() == null || location.getProvinceName() == null || 
            location.getDistrictName() == null) {
            throw new IllegalArgumentException("Province code, province name, and district name are required");
        }
        
        return locationRepository.save(location);
    }

    /**
     * Get location by ID
     */
    public Location getLocationById(Long id) {
        return locationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Location not found with id: " + id));
    }

    /**
     * Get all locations
     */
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    /**
     * REQUIREMENT #7: Check if province exists
     */
    public boolean provinceExists(String provinceCode) {
        return locationRepository.existsByProvinceCode(provinceCode);
    }

    /**
     * REQUIREMENT #7: Check if district exists in province
     */
    public boolean districtExistsInProvince(String provinceCode, String districtName) {
        return locationRepository.existsByProvinceCodeAndDistrictName(provinceCode, districtName);
    }

    /**
     * Get locations by province code
     */
    public List<Location> getLocationsByProvinceCode(String provinceCode) {
        return locationRepository.findByProvinceCode(provinceCode);
    }

    /**
     * REQUIREMENT #3: Get locations by province with Pagination
     */
    public Page<Location> getLocationsByProvinceCode(String provinceCode, Pageable pageable) {
        return locationRepository.findByProvinceCode(provinceCode, pageable);
    }

    /**
     * Get locations by province name
     */
    public List<Location> getLocationsByProvinceName(String provinceName) {
        return locationRepository.findByProvinceName(provinceName);
    }

    /**
     * Get locations by district name
     */
    public List<Location> getLocationsByDistrictName(String districtName) {
        return locationRepository.findByDistrictName(districtName);
    }

    /**
     * Get all unique provinces
     */
    public List<Object[]> getAllProvinces() {
        return locationRepository.findAllProvinces();
    }

    /**
     * Get all districts in a province
     */
    public List<String> getDistrictsByProvinceCode(String provinceCode) {
        return locationRepository.findDistrictsByProvinceCode(provinceCode);
    }

    /**
     * Get all sectors in a district
     */
    public List<String> getSectorsByProvinceAndDistrict(String provinceCode, String districtName) {
        return locationRepository.findSectorsByProvinceAndDistrict(provinceCode, districtName);
    }

    /**
     * Get locations with complete hierarchy
     */
    public List<Location> getCompleteHierarchyLocations() {
        return locationRepository.findCompleteHierarchyLocations();
    }

    /**
     * Get locations by level (District, Sector, Cell, Village)
     */
    public List<Location> getLocationsByLevel(String level) {
        return locationRepository.findByLocationLevel(level);
    }

    /**
     * Search locations by keyword
     */
    public List<Location> searchLocations(String keyword) {
        return locationRepository.searchByKeyword(keyword);
    }

    /**
     * Count locations by province
     */
    public long countLocationsByProvince(String provinceCode) {
        return locationRepository.countByProvinceCode(provinceCode);
    }

    /**
     * Get location statistics
     */
    public LocationStatistics getLocationStatistics() {
        long totalLocations = locationRepository.count();
        List<Object[]> provinces = getAllProvinces();
        long totalProvinces = provinces.size();
        
        // Count unique districts
        long totalDistricts = locationRepository.findAll().stream()
            .map(Location::getDistrictName)
            .distinct()
            .count();
        
        return new LocationStatistics(totalLocations, totalProvinces, totalDistricts);
    }

    /**
     * Inner class for location statistics
     */
    public static class LocationStatistics {
        private final long totalLocations;
        private final long totalProvinces;
        private final long totalDistricts;

        public LocationStatistics(long totalLocations, long totalProvinces, long totalDistricts) {
            this.totalLocations = totalLocations;
            this.totalProvinces = totalProvinces;
            this.totalDistricts = totalDistricts;
        }

        // Getters
        public long getTotalLocations() { return totalLocations; }
        public long getTotalProvinces() { return totalProvinces; }
        public long getTotalDistricts() { return totalDistricts; }
    }
}