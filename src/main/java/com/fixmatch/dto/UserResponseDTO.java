package com.fixmatch.dto;

import com.fixmatch.entity.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UserResponseDTO - Simple DTO for user registration responses
 * 
 * This DTO prevents JSON serialization issues with complex entity relationships
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    
    private Long id;
    private String name;
    private String email;
    private String phone;
    private UserType userType;
    private String locationName;
    private String fullLocationPath;
    
    /**
     * Constructor for basic user info
     */
    public UserResponseDTO(Long id, String name, String email, String phone, UserType userType) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.userType = userType;
    }
}