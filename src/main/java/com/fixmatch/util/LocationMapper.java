package com.fixmatch.util;

import com.fixmatch.dto.LocationDTO;
import com.fixmatch.entity.Location;
import java.util.List;
import java.util.stream.Collectors;

/**
 * LocationMapper - Utility class for converting between Entity and DTO
 * 
 * This mapper prevents JSON serialization issues while providing complete location data
 */
public class LocationMapper {

    /**
     * Convert Location entity to DTO
     */
    public static LocationDTO toDTO(Location entity) {
        if (entity == null) {
            return null;
        }
        
        LocationDTO dto = new LocationDTO();
        dto.setLocationId(entity.getLocationId());
        dto.setName(entity.getName());
        dto.setCode(entity.getCode());
        dto.setType(entity.getType());
        dto.setFullPath(entity.getFullPath());
        dto.setFormattedAddress(entity.getFormattedAddress());
        dto.setDepthLevel(entity.getDepthLevel());
        dto.setRoot(entity.isRoot());
        dto.setLeaf(entity.isLeaf());
        
        // Add parent information if exists
        if (entity.getParentLocation() != null) {
            dto.setParentLocationId(entity.getParentLocation().getLocationId());
            dto.setParentLocationName(entity.getParentLocation().getName());
        }
        
        return dto;
    }
    
    /**
     * Convert list of Location entities to DTOs
     */
    public static List<LocationDTO> toDTOList(List<Location> entities) {
        if (entities == null) {
            return null;
        }
        
        return entities.stream()
                .map(LocationMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Convert DTO to Location entity (for creation/updates)
     */
    public static Location toEntity(LocationDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Location entity = new Location();
        entity.setLocationId(dto.getLocationId());
        entity.setName(dto.getName());
        entity.setCode(dto.getCode());
        entity.setType(dto.getType());
        
        // Note: Parent and children relationships should be set separately
        // to avoid circular reference issues during creation
        
        return entity;
    }
}