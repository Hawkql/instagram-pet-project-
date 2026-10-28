package com.example.Post_Service.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentsRequest(@NotBlank @Size(max = 2200) String comments) {
}
