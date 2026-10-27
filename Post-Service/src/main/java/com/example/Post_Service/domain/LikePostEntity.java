package com.example.Post_Service.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "post_likes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"post_id", "author_id"}) )
public class LikePostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "post_id",nullable = false,unique = true)
    private  UUID postId;
    @Column(name = "liked_author",nullable = false,unique = true)
    private  UUID likedAuthor;

    @Column(name = "created_at",nullable = false)
    @CreationTimestamp
    private Instant createAt;

    public LikePostEntity(UUID postId, UUID likedAuthor) {
        this.postId = postId;
        this.likedAuthor = likedAuthor;
    }

    protected LikePostEntity() {
    }
}
