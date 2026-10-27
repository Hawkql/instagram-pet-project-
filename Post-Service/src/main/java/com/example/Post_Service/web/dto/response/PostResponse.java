package com.example.Post_Service.web.dto.response;

import java.time.Instant;
import java.util.UUID;

public record PostResponse(UUID id,
                           UUID authorId,
                           String caption,
                           String mediaUrl,
                           int likesCount,
                           int commentsCount,
                           boolean likedByMe,
                           Instant createdAt) {
}
