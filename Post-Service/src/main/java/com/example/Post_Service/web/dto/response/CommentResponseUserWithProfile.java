package com.example.Post_Service.web.dto.response;

import com.example.Post_Service.web.dto.request.ShortUserDto;

import java.time.Instant;
import java.time.LocalDateTime;

public record CommentResponseUserWithProfile(ShortUserDto user, String comments, Instant createAt) {
}
