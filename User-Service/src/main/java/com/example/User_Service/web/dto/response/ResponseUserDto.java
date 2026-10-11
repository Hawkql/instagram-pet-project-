package com.example.User_Service.web.dto.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record ResponseUserDto (@NotNull
                               String name,
                               @NotNull
                               String family,
                               @NotNull
                               String userName,
                               @NotNull
                               @Email
                               String email,
                               @NotNull
                               String bio){
}
