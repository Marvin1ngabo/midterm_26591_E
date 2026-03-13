package com.fixmatch.service;

import com.fixmatch.entity.Location;
import com.fixmatch.entity.LocationType;
import com.fixmatch.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * LocationService - Business logic for hierarchical location operations
 * 
 * Demonstrates:
 * 1. Tree data structure operations
 * 2. Hierarchical data management
 * 3. Location path building and navigation
 */
@Service
@Transactional
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    /**
     * Create a new location
     */
    public Location createLocation(String name, String code, LocationType type, Long parentId) {
        Location location = new Location(name, code, type);
        
        if (parentId != null) {
            Location parent = locationRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent location not found with id: " + parentId));
            location.setParentLocation(parent);
        }
        
        return locationRepository.save(location);
    }

    /**
     * Get all root locations (provinces)
     */
    public List<Location> getAllProvinces() {
        return locationRepository.findAllProvinces(LocationType.PROVINCE);
    }

    /**
     * Get all villages
     */
    public List<Location> getAllVillages() {
        return locationRepository.findAllVillages(LocationType.VILLAGE);
    }

    /**
     * Get location by ID
     */
    public Location getLocationById(Long id) {
        return locationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Location not found with id: " + id));
    }

    /**
     * Get location by name and type
     */
    public Optional<Location> getLocationByNameAndType(String name, LocationType type) {
        return locationRepository.findByNameAndType(name, type);
    }

    /**
     * Get all children of a location
     */
    public List<Location> getChildren(Long parentId) {
        return locationRepository.findByParentLocationLocationId(parentId);
    }

    /**
     * Get all locations of a specific type
     */
    public List<Location> getLocationsByType(LocationType type) {
        return locationRepository.findByType(type);
    }

    /**
     * Search locations by name
     */
    public List<Location> searchLocationsByName(String name) {
        return locationRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Build complete Rwanda location hierarchy
     */
    public void buildRwandaHierarchy() {
        // Create Kigali Province
        Location kigali = createLocation("Kigali City", "KGL", LocationType.PROVINCE, null);
        
        // Create Districts under Kigali
        Location gasabo = createLocation("Gasabo", "GAS", LocationType.DISTRICT, kigali.getLocationId());
        Location kicukiro = createLocation("Kicukiro", "KIC", LocationType.DISTRICT, kigali.getLocationId());
        Location nyarugenge = createLocation("Nyarugenge", "NYA", LocationType.DISTRICT, kigali.getLocationId());
        
        // Create Sectors under Gasabo
        Location kimironko = createLocation("Kimironko", "KIM", LocationType.SECTOR, gasabo.getLocationId());
        Location remera = createLocation("Remera", "REM", LocationType.SECTOR, gasabo.getLocationId());
        
        // Create Cells under Kimironko
        Location bibare = createLocation("Bibare", "BIB", LocationType.CELL, kimironko.getLocationId());
        Location kibagabaga = createLocation("Kibagabaga", "KBG", LocationType.CELL, kimironko.getLocationId());
        
        // Create Villages under Bibare
        createLocation("Nyagatovu", "NYG", LocationType.VILLAGE, bibare.getLocationId());
        createLocation("Kiyovu", "KIY", LocationType.VILLAGE, bibare.getLocationId());
        
        // Create more provinces
        Location eastern = createLocation("Eastern Province", "EST", LocationType.PROVINCE, null);
        Location western = createLocation("Western Province", "WST", LocationType.PROVINCE, null);
        Location northern = createLocation("Northern Province", "NTH", LocationType.PROVINCE, null);
        Location southern = createLocation("Southern Province", "STH", LocationType.PROVINCE, null);
        
        // Add some districts to other provinces
        createLocation("Rwamagana", "RWA", LocationType.DISTRICT, eastern.getLocationId());
        createLocation("Karongi", "KAR", LocationType.DISTRICT, western.getLocationId());
        createLocation("Musanze", "MUS", LocationType.DISTRICT, northern.getLocationId());
        createLocation("Huye", "HUY", LocationType.DISTRICT, southern.getLocationId());
    }

    /**
     * Get full path for a location
     */
    public String getFullPath(Long locationId) {
        Location location = getLocationById(locationId);
        return location.getFullPath();
    }

    /**
     * Get location statistics
     */
    public LocationStatistics getLocationStatistics() {
        long totalLocations = locationRepository.count();
        long provinces = locationRepository.findByType(LocationType.PROVINCE).size();
        long districts = locationRepository.findByType(LocationType.DISTRICT).size();
        long sectors = locationRepository.findByType(LocationType.SECTOR).size();
        long cells = locationRepository.findByType(LocationType.CELL).size();
        long villages = locationRepository.findByType(LocationType.VILLAGE).size();
        
        return new LocationStatistics(totalLocations, provinces, districts, sectors, cells, villages);
    }

    /**
     * Find village by name (for user registration)
     */
    public Optional<Location> findVillageByName(String villageName) {
        return locationRepository.findByNameAndType(villageName, LocationType.VILLAGE);
    }

    /**
     * Check if location exists
     */
    public boolean locationExists(String name, LocationType type) {
        return locationRepository.existsByNameAndType(name, type);
    }

    /**
     * Delete location (only if it has no children)
     */
    public void deleteLocation(Long locationId) {
        Location location = getLocationById(locationId);
        
        if (!location.getChildLocations().isEmpty()) {
            throw new RuntimeException("Cannot delete location with children. Delete children first.");
        }
        
        locationRepository.delete(location);
    }

    /**
     * Move location to new parent
     */
    public Location moveLocation(Long locationId, Long newParentId) {
        Location location = getLocationById(locationId);
        Location newParent = getLocationById(newParentId);
        
        location.setParentLocation(newParent);
        return locationRepository.save(location);
    }

    /**
     * Inner class for location statistics
     */
    public static class LocationStatistics {
        private final long totalLocations;
        private final long provinces;
        private final long districts;
        private final long sectors;
        private final long cells;
        private final long villages;

        public LocationStatistics(long totalLocations, long provinces, long districts, 
                                long sectors, long cells, long villages) {
            this.totalLocations = totalLocations;
            this.provinces = provinces;
            this.districts = districts;
            this.sectors = sectors;
            this.cells = cells;
            this.villages = villages;
        }

        // Getters
        public long getTotalLocations() { return totalLocations; }
        public long getProvinces() { return provinces; }
        public long getDistricts() { return districts; }
        public long getSectors() { return sectors; }
        public long getCells() { return cells; }
        public long getVillages() { return villages; }
    }
}