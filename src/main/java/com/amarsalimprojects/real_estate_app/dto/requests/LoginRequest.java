package com.amarsalimprojects.real_estate_app.dto.requests;

import lombok.Data;

@Data
public class LoginRequest {

    private String email;
    private String username;
    private String password;

}
