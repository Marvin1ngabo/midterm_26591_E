package com.fixmatch.controller;

import com.fixmatch.entity.HierarchicalLocation;
import com.fixmatch.entity.LocationType;
import com.fixmatch.service.HierarchicalLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * HierarchicalLocationController - REST API endpoints for hierarchical location operations
 * 
 * Base URL: /api/hierarchical-locations
 */
@RestController
@RequestMapping("/api/hierarchical-locations")
@CrossOrigin(origins = "*")
public class HierarchicalLocationController {

    @Autowired
    private HierarchicalLocationService locationService;

    /**
     * Initialize Rwanda location hierarchy
     * 
     * POST /api/hierarchical-locations/initialize
     */
    @PostMapping("/initialize")
    public ResponseEntity<String> initializeHierarchy() {
        try {
            locationService.buildRwandaHierarchy();
            return ResponseEntity.ok("Rwanda location hierarchy initialized successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error initializing hierarchy: " + e.getMessage());
        }
    }

    /**
     * Get all provinces (root locations)
     * 
     * GET /api/hierarchical-locations/provinces
     */
    @GetMapping("/provinces")
    public ResponseEntity<List<HierarchicalLocation>> getAllProvinces() {
        List<HierarchicalLocation> provinces = locationService.getAllProvinces();
        return ResponseEntity.ok(provinces);
    }

    /**
     * Get all villages
     * 
     * GET /api/hierarchical-locations/villages
     */
    @GetMapping("/villages")
    public ResponseEntity<List<HierarchicalLocation>> getAllVillages() {
        List<HierarchicalLocation> villages = locationService.getAllVillages();
        return ResponseEntity.ok(villages);
    }

    /**
     * Get location by ID with full path
     * 
     * GET /api/hierarchical-locations/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<LocationResponse> getLocationById(@PathVariable Long id) {
        try {
            HierarchicalLocation location = locationService.getLocationById(id);
            LocationResponse response = new LocationResponse(location);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get children of a location
     * 
     * GET /api/hierarchical-locations/1/children
     */
    @GetMapping("/{id}/children")
    public ResponseEntity<List<HierarchicalLocation>> getChildren(@PathVariable Long id) {
        List<HierarchicalLocation> children = locationService.getChildren(id);
        return ResponseEntity.ok(children);
    }

    /**
     * Get locations by type
     * 
     * GET /api/hierarchical-locations/type/DISTRICT
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<HierarchicalLocation>> getLocationsByType(@PathVariable LocationType type) {
        List<HierarchicalLocation> locations = locationService.getLocationsByType(type);
        return ResponseEntity.ok(locations);
    }

    /**
     * Search locations by name
     * 
     * GET /api/hierarchical-locations/search?name=Kigali
     */
    @GetMapping("/search")
    public ResponseEntity<List<HierarchicalLocation>> searchLocations(@RequestParam String name) {
        List<HierarchicalLocation> locations = locationService.searchLocationsByName(name);
        return ResponseEntity.ok(locations);
    }

    /**
     * Create new location
     * 
     * POST /api/hierarchical-locations
     */
    @PostMapping
    public ResponseEntity<HierarchicalLocation> createLocation(@RequestBody CreateLocationRequest request) {
        try {
            HierarchicalLocation location = locationService.createLocation(
                request.getName(), 
                request.getCode(), 
                request.getType(), 
                request.getParentId()
            );
            return new ResponseEntity<>(location, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get location statistics
     * 
     * GET /api/hierarchical-locations/statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<HierarchicalLocationService.LocationStatistics> getStatistics() {
        HierarchicalLocationService.LocationStatistics stats = locationService.getLocationStatistics();
        return ResponseEntity.ok(stats);
    }

    /**
     * Get full path for a location
     * 
     * GET /api/hierarchical-locations/1/path
     */
    @GetMapping("/{id}/path")
    public ResponseEntity<String> getFullPath(@PathVariable Long id) {
        try {
            String path = locationService.getFullPath(id);
            return ResponseEntity.ok(path);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Find village by name (for user registration)
     * 
     * GET /api/hierarchical-locations/village/Kiyovu
     */
    @GetMapping("/village/{name}")
    public ResponseEntity<LocationResponse> findVillageByName(@PathVariable String name) {
        Optional<HierarchicalLocation> village = locationService.findVillageByName(name);
        if (village.isPresent()) {
            LocationResponse response = new LocationResponse(village.get());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Response DTO for location with full path information
     */
    public static class LocationResponse {
        private Long locationId;
        private String name;
        private String code;
        private LocationType type;
        private String fullPath;
        private String formattedAddress;
        private int depthLevel;
        private boolean isRoot;
        private boolean isLeaf;

        public LocationResponse(HierarchicalLocation location) {
            this.locationId = location.getLocationId();
            this.name = location.getName();
            this.code = location.getCode();
            this.type = location.getType();
            this.fullPath = location.getFullPath();
            this.formattedAddress = location.getFormattedAddress();
            this.depthLevel = location.getDepthLevel();
            this.isRoot = location.isRoot();
            this.isLeaf = location.isLeaf();
        }

        // Getters
        public Long getLocationId() { return locationId; }
        public String getName() { return name; }
        public String getCode() { return code; }
        public LocationType getType() { return type; }
        public String getFullPath() { return fullPath; }
        public String getFormattedAddress() { return formattedAddress; }
        public int getDepthLevel() { return depthLevel; }
        public boolean isRoot() { return isRoot; }
        public boolean isLeaf() { return isLeaf; }
    }

    /**
     * Request DTO for creating locations
     */
    public static class CreateLocationRequest {
        private String name;
        private String code;
        private LocationType type;
        private Long parentId;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public LocationType getType() { return type; }
        public void setType(LocationType type) { this.type = type; }
        public Long getParentId() { return parentId; }
        public void setParentId(Long parentId) { this.parentId = parentId; }
    }
}