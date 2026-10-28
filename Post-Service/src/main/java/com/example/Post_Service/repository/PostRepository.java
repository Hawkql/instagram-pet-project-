package com.example.Post_Service.repository;

import com.example.Post_Service.domain.PostEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface PostRepository extends JpaRepository<PostEntity,UUID> {
    @Query("Select f From PostEntity f where f.authorId =:authorId")
    Slice<PostEntity> findByAuthorId(@Param("authorId") UUID authorId, Pageable page);

    @Query("Select p From PostEntity p where p.id =:id and p.authorId =:authorId")
    PostEntity findByIdAndAuthorId(@Param("authorId") UUID authorId,
                                   @Param("id") UUID id);

    @Modifying
    @Query("UPDATE PostEntity p SET p.likesCount = p.likesCount + 1 WHERE p.id = :postId")
    void incrementLikes(@Param("postId") UUID postId);

    @Modifying
    @Query("UPDATE PostEntity p SET p.likesCount = p.likesCount - 1 WHERE p.id = :postId")
    void decrementLikesCount(@Param("postId") UUID postId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE PostEntity p SET p.commentsCount = p.commentsCount + 1 WHERE p.id = :postId")
    void incrementCommentsCount(@Param("postId") UUID postId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE PostEntity p SET p.commentsCount = p.commentsCount - 1 WHERE p.id = :postId")
    void decrementCommentsCount(@Param("postId") UUID postId);



}
