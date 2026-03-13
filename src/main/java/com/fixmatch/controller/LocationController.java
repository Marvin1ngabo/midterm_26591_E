package com.fixmatch.controller;

import com.fixmatch.dto.LocationDTO;
import com.fixmatch.entity.Location;
import com.fixmatch.entity.LocationType;
import com.fixmatch.service.LocationService;
import com.fixmatch.util.LocationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * LocationController - REST API for hierarchical location operations
 * 
 * Demonstrates:
 * 1. Tree data structure operations via REST API
 * 2. Hierarchical location management
 * 3. Self-referencing relationship handling
 * 4. DTO pattern for JSON serialization
 */
@RestController
@RequestMapping("/api/locations")
@CrossOrigin(origins = "*")
public class LocationController {

    @Autowired
    private LocationService locationService;

    /**
     * Initialize Rwanda administrative hierarchy
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
     * Get all provinces (root nodes)
     */
    @GetMapping("/provinces")
    public ResponseEntity<List<LocationDTO>> getAllProvinces() {
        try {
            List<Location> provinces = locationService.getAllProvinces();
            List<LocationDTO> provinceDTOs = LocationMapper.toDTOList(provinces);
            return ResponseEntity.ok(provinceDTOs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get all villages (leaf nodes)
     */
    @GetMapping("/villages")
    public ResponseEntity<List<LocationDTO>> getAllVillages() {
        try {
            List<Location> villages = locationService.getAllVillages();
            List<LocationDTO> villageDTOs = LocationMapper.toDTOList(villages);
            return ResponseEntity.ok(villageDTOs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get location by ID with full hierarchy information
     */
    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> getLocationById(@PathVariable Long id) {
        try {
            Location location = locationService.getLocationById(id);
            LocationDTO locationDTO = LocationMapper.toDTO(location);
            return ResponseEntity.ok(locationDTO);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get children of a specific location
     */
    @GetMapping("/{id}/children")
    public ResponseEntity<List<LocationDTO>> getChildren(@PathVariable Long id) {
        try {
            List<Location> children = locationService.getChildren(id);
            List<LocationDTO> childrenDTOs = LocationMapper.toDTOList(children);
            return ResponseEntity.ok(childrenDTOs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get full hierarchy path for a location
     */
    @GetMapping("/{id}/path")
    public ResponseEntity<String> getFullPath(@PathVariable Long id) {
        try {
            String fullPath = locationService.getFullPath(id);
            return ResponseEntity.ok(fullPath);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get locations by type
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<LocationDTO>> getLocationsByType(@PathVariable LocationType type) {
        try {
            List<Location> locations = locationService.getLocationsByType(type);
            List<LocationDTO> locationDTOs = LocationMapper.toDTOList(locations);
            return ResponseEntity.ok(locationDTOs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Search locations by name
     */
    @GetMapping("/search")
    public ResponseEntity<List<LocationDTO>> searchLocations(@RequestParam String name) {
        try {
            List<Location> locations = locationService.searchLocationsByName(name);
            List<LocationDTO> locationDTOs = LocationMapper.toDTOList(locations);
            return ResponseEntity.ok(locationDTOs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Find village by name (for user registration)
     */
    @GetMapping("/village/{villageName}")
    public ResponseEntity<LocationDTO> findVillageByName(@PathVariable String villageName) {
        try {
            Optional<Location> village = locationService.findVillageByName(villageName);
            if (village.isPresent()) {
                LocationDTO villageDTO = LocationMapper.toDTO(village.get());
                return ResponseEntity.ok(villageDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get hierarchy statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<LocationService.LocationStatistics> getStatistics() {
        try {
            LocationService.LocationStatistics stats = locationService.getLocationStatistics();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Create new location
     */
    @PostMapping
    public ResponseEntity<LocationDTO> createLocation(@RequestBody CreateLocationRequest request) {
        try {
            Location location = locationService.createLocation(
                request.getName(), 
                request.getCode(), 
                request.getType(), 
                request.getParentId()
            );
            LocationDTO locationDTO = LocationMapper.toDTO(location);
            return ResponseEntity.ok(locationDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Delete location (only if no children)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLocation(@PathVariable Long id) {
        try {
            locationService.deleteLocation(id);
            return ResponseEntity.ok("Location deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting location: " + e.getMessage());
        }
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