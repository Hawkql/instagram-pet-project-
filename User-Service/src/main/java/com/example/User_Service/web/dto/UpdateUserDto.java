package com.example.User_Service.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UpdateUserDto(@NotNull
                            String name,
                            @NotNull
                            String family,
                            @NotNull
                            String bio) {
}
