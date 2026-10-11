package com.example.User_Service.repository;

import com.example.User_Service.domain.FollowEntity;
import com.example.User_Service.web.dto.response.FollowerDto;
import com.example.User_Service.web.dto.response.FollowerDtoResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, UUID> {
    @Modifying
    @Query("delete from FollowEntity f where f.followerId = :follower and f.followeeId = :followee")
    int deleteByFollowerIdAndFolloweeId(@Param("follower") UUID follower
            ,@Param("followee") UUID followee);

    @Query("Select f from FollowEntity f where f.followerId=:follower and  f.followeeId = :followee")
    FollowEntity findFollowerIdAndFolloweeId(
            @Param("follower")UUID follower,
            @Param("followee")UUID followee);


    @Query("""
        Select new com.example.User_Service.web.dto.response.FollowerDto(u.id,u.userName,u.avatarUrl)
        From UserEntity u
        join FollowEntity f on f.followerId=u.id
        where f.followeeId =:id
        """)
    Slice<FollowerDto> findFollowers(@Param("id") UUID id, Pageable pageable);

    @Query("""
        Select new com.example.User_Service.web.dto.response.FollowerDto(u.id,u.userName,u.avatarUrl)
        From UserEntity u
        join FollowEntity f on f.followeeId=u.id
        where f.followerId =:id
        """)
    Slice<FollowerDto> findFollowing(@Param("id") UUID id, Pageable pageable);
}
