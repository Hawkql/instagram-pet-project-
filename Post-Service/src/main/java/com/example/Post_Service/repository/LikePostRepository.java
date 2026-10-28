package com.example.Post_Service.repository;

import com.example.Post_Service.domain.LikePostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;
@Repository
public interface LikePostRepository extends JpaRepository<LikePostEntity, UUID> {


    @Query("""
        select l.postId
        from LikePostEntity l
        where l.likedAuthor = :userId
        and l.postId in :postIds
        """)
    Set<UUID> findLikedPostIds(@Param("userId") UUID currenIdUser, Set<UUID> postIds);

    boolean existsByPostIdAndLikedAuthor(UUID postId, UUID userId);


    @Modifying
    @Query("delete from LikePostEntity l where l.postId = :postId and l.likedAuthor = :userId")
    int deleteByPostIdAndLikedAuthor(UUID postId, UUID userId);
}
