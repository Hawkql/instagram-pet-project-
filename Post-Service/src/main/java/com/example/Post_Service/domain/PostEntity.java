package com.example.Post_Service.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
@Table(name = "posts")
@Getter
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "author_id",nullable = false)
    private UUID authorId;

    @Setter
    @Column(name = "caption",nullable = true,unique = false)
    private  String caption;

    @Column(name = "like_count")
    private Integer likesCount;
    @Column(name = "comment_count")
    private  Integer commentsCount;

    @Column(name= "media_key")
    private  String media;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    //придумать со статусом что делать
    private PostStatus status;
    @Column(name ="created_at",nullable = false)
    @CreationTimestamp
    private Instant createdAt;

    public PostEntity(UUID authorId,
                      String caption,
                      String media
                      ) {
        this.authorId = authorId;
        this.caption = caption;
        this.media = media;
        this.status = PostStatus.DRAFT;
        this.likesCount = 0;
        this.commentsCount = 0;
    }

    protected PostEntity() {
    }
}
