package com.example.User_Service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Table(name = "follows")
public class FollowEntity {

    @Id
    private  UUID id;

    @Column(name = "follower_id",nullable = false)
    private UUID followerId;

    @Column(name = "followee_id",nullable = false)
    private UUID followeeId;

    @Column(name = "created_at",nullable = false)
    @CreationTimestamp
    private Instant createdAt;

    protected FollowEntity() {
    }

    public FollowEntity( UUID followerId, UUID followeeId) {
        this.id = UUID.randomUUID();
        this.followerId = followerId;
        this.followeeId = followeeId;
    }
}
