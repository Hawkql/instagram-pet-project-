package com.example.Post_Service.web.dto.request;

import java.util.UUID;

public record LikedPostRequestDto(UUID idPost,UUID authorId) {
}
