package com.example.User_Service.web.dto;

import java.time.LocalDateTime;

public record ExceptionDto(
        String message,
        String detailMessage,
        LocalDateTime dateTime
) {
}
