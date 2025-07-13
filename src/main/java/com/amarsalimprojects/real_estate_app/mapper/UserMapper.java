package com.amarsalimprojects.real_estate_app.mapper;

import com.amarsalimprojects.real_estate_app.dto.responses.UserResponse;
import com.amarsalimprojects.real_estate_app.model.User;

public class UserMapper {

    public static UserResponse toUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setRole(user.getRole().name());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());

        // Use Optional to handle null BuyerProfile
        userResponse.setBuyerId(
                user.getBuyerProfile() != null ? user.getBuyerProfile().getId() : null
        );

        return userResponse;
    }
}
