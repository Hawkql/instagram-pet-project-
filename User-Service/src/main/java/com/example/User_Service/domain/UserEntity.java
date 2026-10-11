package com.example.User_Service.domain;

import com.example.User_Service.web.dto.request.UpdateUserDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
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
    @Column(name = "created_at",nullable = false)
    @CreationTimestamp
    private Instant createdAt;


    public UserEntity(UUID id,String name, String family, String userName, String email, String bio) {
        this.id = id ;
        this.name = name;
        this.family = family;
        this.userName = userName;
        this.email = email;
        this.bio = bio;
    }

    public UserEntity() {
    }

    public  void update(UpdateUserDto dto){
        this.name=dto.name();
        this.family= dto.family();
        this.bio= dto.bio();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
