package com.example.User_Service.web.dto.response;

import java.util.UUID;

public record FollowerDto(UUID id,
                          String userName,
                          String avatarKey) {
}
