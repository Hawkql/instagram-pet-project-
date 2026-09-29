package com.example.User_Service.domain;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @Setter
    @Column(name = "name",nullable = false)
    private String name;

    @Setter
    @Column(name = "family",nullable = false)
    private String family;

    @Column(name = "user_name",nullable = false)
    private String userName;

    @Setter
    @Column(name = "email",nullable = false,unique = true)
    private String email;

    @Setter
    @Column(name = "bio")
    private  String bio;

    @Setter
    @Column(name = "avatar_url")
    private String avatarUrl;
    @Column(name = "created",nullable = false)
    private LocalDate createdAt;


    public UserEntity(String name, String family, String userName, String email, String bio, LocalDate createdAt) {
        this.name = name;
        this.family = family;
        this.userName = userName;
        this.email = email;
        this.bio = bio;
        this.createdAt = createdAt;
    }

    public UserEntity() {
    }
}
