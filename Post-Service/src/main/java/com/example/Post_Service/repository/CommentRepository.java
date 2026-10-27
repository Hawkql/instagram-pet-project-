package com.example.Post_Service.repository;

import com.example.Post_Service.domain.CommentsEntity;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<CommentsEntity, UUID> {
    List<CommentsEntity> findByPostId(UUID postId, Pageable page);
    @Query("""
        Select new com.example.User_Service.web.dto.response.FollowerDto(u.id,u.userName,u.avatarUrl)
        From UserEntity u
        join FollowEntity f on f.followerId=u.id
        where f.followeeId =:id
        """)

    Slice<CommentsEntity> findByIdPost(UUID postId, PageableDefault page);
}
