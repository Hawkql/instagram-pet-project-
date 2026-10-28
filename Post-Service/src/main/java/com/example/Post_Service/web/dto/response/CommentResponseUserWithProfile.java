package com.example.Post_Service.web.dto.response;

import com.example.Post_Service.web.dto.request.ShortUserDto;

import java.time.Instant;
import java.util.UUID;

public record CommentResponseUserWithProfile(UUID id, ShortUserDto user, String comments, Instant createAt) {
}
