package com.example.Post_Service.web.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DeleteCommentRequest(@NotNull UUID postId, @NotNull UUID commentsId) {
}
