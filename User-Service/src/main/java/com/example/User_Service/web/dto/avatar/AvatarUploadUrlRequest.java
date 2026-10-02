package com.example.User_Service.web.dto.avatar;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record AvatarUploadUrlRequest(
    @NotBlank String contentType
) {
}
