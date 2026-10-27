package com.example.Post_Service.web.dto.request;

import java.util.UUID;

public record UpdateCommentsRequest(UUID postId,UUID userId,UUID commentsId,String comments) {
}
