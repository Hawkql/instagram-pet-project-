package com.example.User_Service.web.dto.request;

import jakarta.validation.constraints.*;

import java.util.UUID;

public record RequestUserDto(

        UUID id,
        @NotNull
        String name,
        @NotNull
        String family,
        @NotNull
        @Size(min = 2,max = 25)
        String userName,
        @NotNull
        @Email
        String email,
        @Size(max = 255)
        String bio) {
}
