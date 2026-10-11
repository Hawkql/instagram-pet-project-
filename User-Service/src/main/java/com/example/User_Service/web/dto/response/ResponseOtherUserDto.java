package com.example.User_Service.web.dto.response;

public record ResponseOtherUserDto(
        String name,
        String family,
        String userName,
        String  bio,
        String url
) {
}
