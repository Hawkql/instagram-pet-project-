package com.example.User_Service.web.dto.response;

import java.util.UUID;

public record FollowerDtoResponse(
        UUID id,
        String userName,
        String avatarUrl){
}
