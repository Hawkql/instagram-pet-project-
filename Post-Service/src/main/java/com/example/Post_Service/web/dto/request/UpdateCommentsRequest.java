package com.example.Post_Service.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record UpdateCommentsRequest(@NotNull UUID postId,
                                     @NotNull UUID userId,
                                     @NotNull UUID commentsId,
                                     @NotBlank @Size(max = 2200) String comments) {
}
