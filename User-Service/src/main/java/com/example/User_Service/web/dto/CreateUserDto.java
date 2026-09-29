package com.example.User_Service.web.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public record CreateUserDto(
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
