package com.fixmatch.controller;

import com.fixmatch.entity.Location;
import com.fixmatch.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * LocationController - REST API endpoints for Location entity
 * 
 * Base URL: /api/locations
 */
@RestController
@RequestMapping("/api/locations")
@CrossOrigin(origins = "*")
public class LocationController {

    @Autowired
    private LocationService locationService;

    /**
     * REQUIREMENT #2: Save Location
     * 
     * POST /api/locations
     * Body: { "provinceCode": "KGL", "provinceName": "Kigali", "districtName": "Gasabo", "sectorName": "Kimisagara", "cellName": "Rugenge", "villageName": "Kiyovu" }
     */
    @PostMapping
    public ResponseEntity<Location> createLocation(@RequestBody Location location) {
        try {
            Location saved = locationService.saveLocation(location);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get all locations
     * 
     * GET /api/locations
     */
    @GetMapping
    public ResponseEntity<List<Location>> getAllLocations() {
        List<Location> locations = locationService.getAllLocations();
        return ResponseEntity.ok(locations);
    }

    /**
     * Get location by ID
     * 
     * GET /api/locations/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationById(@PathVariable Long id) {
        try {
            Location location = locationService.getLocationById(id);
            return ResponseEntity.ok(location);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get all unique provinces
     * 
     * GET /api/locations/provinces
     */
    @GetMapping("/provinces")
    public ResponseEntity<List<Object[]>> getAllProvinces() {
        List<Object[]> provinces = locationService.getAllProvinces();
        return ResponseEntity.ok(provinces);
    }

    /**
     * Get locations by province code
     * 
     * GET /api/locations/province/{code}
     */
    @GetMapping("/province/{code}")
    public ResponseEntity<List<Location>> getLocationsByProvinceCode(@PathVariable String code) {
        List<Location> locations = locationService.getLocationsByProvinceCode(code);
        return ResponseEntity.ok(locations);
    }

    /**
     * REQUIREMENT #3: Get locations by province with Pagination
     * 
     * GET /api/locations/province/{code}/paginated?page=0&size=10
     */
    @GetMapping("/province/{code}/paginated")
    public ResponseEntity<Page<Location>> getLocationsByProvinceCodePageable(
            @PathVariable String code,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Location> locations = locationService.getLocationsByProvinceCode(code, pageable);
        return ResponseEntity.ok(locations);
    }

    /**
     * Get districts by province code
     * 
     * GET /api/locations/province/{code}/districts
     */
    @GetMapping("/province/{code}/districts")
    public ResponseEntity<List<String>> getDistrictsByProvinceCode(@PathVariable String code) {
        List<String> districts = locationService.getDistrictsByProvinceCode(code);
        return ResponseEntity.ok(districts);
    }

    /**
     * Get sectors by province and district
     * 
     * GET /api/locations/province/{code}/district/{district}/sectors
     */
    @GetMapping("/province/{code}/district/{district}/sectors")
    public ResponseEntity<List<String>> getSectorsByProvinceAndDistrict(
            @PathVariable String code, 
            @PathVariable String district) {
        List<String> sectors = locationService.getSectorsByProvinceAndDistrict(code, district);
        return ResponseEntity.ok(sectors);
    }

    /**
     * REQUIREMENT #7: Check if province exists
     * 
     * GET /api/locations/province/{code}/exists
     */
    @GetMapping("/province/{code}/exists")
    public ResponseEntity<Boolean> provinceExists(@PathVariable String code) {
        boolean exists = locationService.provinceExists(code);
        return ResponseEntity.ok(exists);
    }

    /**
     * REQUIREMENT #7: Check if district exists in province
     * 
     * GET /api/locations/province/{code}/district/{district}/exists
     */
    @GetMapping("/province/{code}/district/{district}/exists")
    public ResponseEntity<Boolean> districtExistsInProvince(
            @PathVariable String code, 
            @PathVariable String district) {
        boolean exists = locationService.districtExistsInProvince(code, district);
        return ResponseEntity.ok(exists);
    }

    /**
     * Get locations by level
     * 
     * GET /api/locations/level/{level}
     */
    @GetMapping("/level/{level}")
    public ResponseEntity<List<Location>> getLocationsByLevel(@PathVariable String level) {
        List<Location> locations = locationService.getLocationsByLevel(level);
        return ResponseEntity.ok(locations);
    }

    /**
     * Search locations by keyword
     * 
     * GET /api/locations/search?keyword=gasabo
     */
    @GetMapping("/search")
    public ResponseEntity<List<Location>> searchLocations(@RequestParam String keyword) {
        List<Location> locations = locationService.searchLocations(keyword);
        return ResponseEntity.ok(locations);
    }

    /**
     * Get location statistics
     * 
     * GET /api/locations/statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<LocationService.LocationStatistics> getLocationStatistics() {
        LocationService.LocationStatistics stats = locationService.getLocationStatistics();
        return ResponseEntity.ok(stats);
    }

    /**
     * Get complete hierarchy locations
     * 
     * GET /api/locations/complete
     */
    @GetMapping("/complete")
    public ResponseEntity<List<Location>> getCompleteHierarchyLocations() {
        List<Location> locations = locationService.getCompleteHierarchyLocations();
        return ResponseEntity.ok(locations);
    }

    /**
     * Get all villages for user registration
     * 
     * GET /api/locations/villages
     */
    @GetMapping("/villages")
    public ResponseEntity<List<Location>> getAllVillageLocations() {
        List<Location> villages = locationService.getAllVillageLocations();
        return ResponseEntity.ok(villages);
    }

    /**
     * Get all village names only
     * 
     * GET /api/locations/villages/names
     */
    @GetMapping("/villages/names")
    public ResponseEntity<List<String>> getAllVillageNames() {
        List<String> villageNames = locationService.getAllVillageNames();
        return ResponseEntity.ok(villageNames);
    }
}
