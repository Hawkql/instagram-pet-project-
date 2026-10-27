package com.example.Post_Service.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "comments")
@Getter
public class CommentsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "post_id",nullable = false)
    private UUID idPost;
    @Column(name = "user_id",nullable = false)
    private UUID user;
    @Setter
    @Column(name = "comment",nullable = false)
    private String  comment;
    @Column(name = "created_at",nullable = false)
    @CreationTimestamp
    private Instant createAt;

    public CommentsEntity(UUID idPost, UUID user, String comment) {

        this.idPost = idPost;
        this.user = user;
        this.comment = comment;
    }

    protected CommentsEntity() {
    }
}
