package com.amarsalimprojects.real_estate_app.dto.responses;

import lombok.Data;

@Data
public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private String role;
    private String firstName;
    private String lastName;
    private Long buyerId;
}
