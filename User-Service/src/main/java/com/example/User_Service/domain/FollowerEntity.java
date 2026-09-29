package com.example.User_Service.domain;

import jakarta.persistence.*;
import org.hibernate.validator.constraints.UUID;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "follows",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"follower_id", "following_id"}
                )
        }
)
public class FollowerEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    private UserEntity follower;

    @ManyToOne
    @JoinColumn(name = "following_id")
    private UserEntity following;

    private LocalDateTime createdAt;

}
