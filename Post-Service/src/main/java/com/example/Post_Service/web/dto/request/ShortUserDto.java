package com.example.Post_Service.web.dto.request;

import java.util.UUID;

public record ShortUserDto(

        // возможно лишние данные возвращаются
        UUID id,
        String userName,
        String url
) {
}
