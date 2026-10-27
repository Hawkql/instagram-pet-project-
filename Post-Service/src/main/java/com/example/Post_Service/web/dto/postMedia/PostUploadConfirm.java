package com.example.Post_Service.web.dto.postMedia;

import jakarta.validation.constraints.NotBlank;

public record PostUploadConfirm(@NotBlank String key) {
}
