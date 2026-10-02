package com.example.User_Service.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RequestUserDto(

        UUID id,
        @NotNull
        String name,
        @NotNull
        String family,
        @NotNull
        String userName,
        @NotNull
        @Email
        String email,
        @NotNull
        String bio) {
}
