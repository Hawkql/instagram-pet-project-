package com.example.Post_Service.web.dto.request;

public record PostRequest(
        String userName,
        String caption,
        String key
) {
}
