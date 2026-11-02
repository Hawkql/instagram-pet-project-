package com.example.Post_Service.repository;

import com.example.Post_Service.domain.PostEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


@Repository
public interface PostRepository extends JpaRepository<PostEntity,UUID> {
    @Query("Select f From PostEntity f where f.authorId =:authorId")
    Slice<PostEntity> findByAuthorId(@Param("authorId") UUID authorId, Pageable page);

    @Query("Select p From PostEntity p where p.id =:id and p.authorId =:authorId")
    Optional<PostEntity> findByIdAndAuthorId(@Param("authorId") UUID authorId,
                                 @Param("id") UUID id);

    @Modifying
    @Query("UPDATE PostEntity p SET p.likesCount = p.likesCount + 1 WHERE p.id = :postId And p.deletedAt IS NULL")
    void incrementLikes(@Param("postId") UUID postId);

    @Modifying
    @Query("UPDATE PostEntity p SET p.likesCount = p.likesCount - 1 WHERE p.id = :postId And p.deletedAt IS NULL")
    void decrementLikesCount(@Param("postId") UUID postId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE PostEntity p SET p.commentsCount = p.commentsCount + 1 WHERE p.id = :postId and p.deletedAt IS NULL")
    void incrementCommentsCount(@Param("postId") UUID postId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE PostEntity p SET p.commentsCount = p.commentsCount - 1 WHERE p.id = :postId and p.deletedAt IS NULL")
    void decrementCommentsCount(@Param("postId") UUID postId);

    @NativeQuery("""
            select p.media_key from posts p
            where p.deleted_at is null or p.deleted_at >= :targetDate
            """)
    Set<String> findAllMediaUrlPost(@Param("targetDate") Instant targetDate);


    @Modifying
    @NativeQuery("delete from posts where deleted_at < :targetDate")
    int deleteSoftDeletedBefore(@Param("targetDate") Instant targetDate);
}
