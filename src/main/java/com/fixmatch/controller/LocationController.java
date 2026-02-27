package com.fixmatch.controller;

import com.fixmatch.entity.District;
import com.fixmatch.entity.Province;
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
 * LocationController - REST API endpoints for Province and District
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
     * REQUIREMENT #2: Save Province
     * 
     * POST /api/locations/provinces
     * Body: { "code": "KGL", "name": "Kigali" }
     */
    @PostMapping("/provinces")
    public ResponseEntity<Province> createProvince(@RequestBody Province province) {
        try {
            Province saved = locationService.saveProvince(province);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get all provinces
     * 
     * GET /api/locations/provinces
     */
    @GetMapping("/provinces")
    public ResponseEntity<List<Province>> getAllProvinces() {
        List<Province> provinces = locationService.getAllProvinces();
        return ResponseEntity.ok(provinces);
    }

    /**
     * Get province by code
     * 
     * GET /api/locations/provinces/code/{code}
     */
    @GetMapping("/provinces/code/{code}")
    public ResponseEntity<Province> getProvinceByCode(@PathVariable String code) {
        try {
            Province province = locationService.getProvinceByCode(code);
            return ResponseEntity.ok(province);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * REQUIREMENT #7: Check if province exists
     * 
     * GET /api/locations/provinces/exists/{code}
     */
    @GetMapping("/provinces/exists/{code}")
    public ResponseEntity<Boolean> provinceExists(@PathVariable String code) {
        boolean exists = locationService.provinceExists(code);
        return ResponseEntity.ok(exists);
    }

    /**
     * REQUIREMENT #2: Save District
     * 
     * POST /api/locations/districts?provinceId=1
     * Body: { "name": "Gasabo" }
     */
    @PostMapping("/districts")
    public ResponseEntity<District> createDistrict(
            @RequestBody District district,
            @RequestParam Long provinceId) {
        try {
            District saved = locationService.saveDistrict(district, provinceId);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get districts by province (without pagination)
     * 
     * GET /api/locations/provinces/{provinceId}/districts
     */
    @GetMapping("/provinces/{provinceId}/districts")
    public ResponseEntity<List<District>> getDistrictsByProvince(@PathVariable Long provinceId) {
        List<District> districts = locationService.getDistrictsByProvince(provinceId);
        return ResponseEntity.ok(districts);
    }

    /**
     * REQUIREMENT #3: Get districts by province with Pagination
     * 
     * GET /api/locations/provinces/{provinceId}/districts/paginated?page=0&size=10
     */
    @GetMapping("/provinces/{provinceId}/districts/paginated")
    public ResponseEntity<Page<District>> getDistrictsByProvincePageable(
            @PathVariable Long provinceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<District> districts = locationService.getDistrictsByProvince(provinceId, pageable);
        return ResponseEntity.ok(districts);
    }
}
