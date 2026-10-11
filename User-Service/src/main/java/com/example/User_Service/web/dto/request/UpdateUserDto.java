package com.example.User_Service.web.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateUserDto(@NotNull
                            String name,
                            @NotNull
                            String family,
                            @NotNull
                            String bio) {
}
