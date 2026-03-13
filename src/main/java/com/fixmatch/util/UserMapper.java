package com.fixmatch.util;

import com.fixmatch.dto.UserResponseDTO;
import com.fixmatch.entity.User;

/**
 * UserMapper - Utility class for converting between User entity and DTOs
 */
public class UserMapper {

    /**
     * Convert User entity to UserResponseDTO
     */
    public static UserResponseDTO toResponseDTO(User user) {
        if (user == null) {
            return null;
        }
        
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setUserType(user.getUserType());
        
        // Set location information
        if (user.getLocation() != null) {
            dto.setLocationName(user.getLocation().getName());
            dto.setFullLocationPath(user.getLocation().getFormattedAddress());
        }
        
        return dto;
    }
}