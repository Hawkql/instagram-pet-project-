package com.example.User_Service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

public record User(
                    String name,
                   String family,
                    String userName,
                    String email,
                    String bio,
                   String avatarUrl,
                   LocalDate createdAt) {
}
