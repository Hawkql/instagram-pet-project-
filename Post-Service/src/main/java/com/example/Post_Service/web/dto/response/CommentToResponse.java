package com.example.Post_Service.web.dto.response;

import java.util.UUID;

public record CommentsResponse(UUID userId,String comment) {
}
