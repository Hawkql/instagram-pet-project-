package com.example.Post_Service.repository;

import com.example.Post_Service.domain.CommentsEntity;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<CommentsEntity, UUID> {
    @Query("""
       SELECT c
       FROM CommentsEntity c
       WHERE c.idPost = :postId
       """)
    Slice<CommentsEntity> findByIdPost(@Param("postId") UUID postId, Pageable pageable);
}
