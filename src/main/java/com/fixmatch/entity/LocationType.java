package com.fixmatch.entity;

/**
 * LocationType Enum - Defines the hierarchical levels of locations
 * 
 * Hierarchy: PROVINCE → DISTRICT → SECTOR → CELL → VILLAGE
 */
public enum LocationType {
    PROVINCE,
    DISTRICT,
    SECTOR,
    CELL,
    VILLAGE
}