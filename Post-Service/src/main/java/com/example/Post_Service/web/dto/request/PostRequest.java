package com.example.Post_Service.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostRequest(
        @Size(max = 2200) String caption,
        @NotBlank String key
) {
}
