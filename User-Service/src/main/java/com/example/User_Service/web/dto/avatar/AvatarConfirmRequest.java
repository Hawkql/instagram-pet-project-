package com.example.User_Service.web.dto.avatar;

import jakarta.validation.constraints.NotBlank;

public record AvatarConfirmRequest(
        @NotBlank String key) {
}
