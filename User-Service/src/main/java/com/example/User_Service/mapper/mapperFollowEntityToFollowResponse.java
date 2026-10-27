package com.example.User_Service.mapper;

import com.example.User_Service.domain.FollowEntity;
import com.example.User_Service.web.dto.response.FollowerDto;
import com.example.User_Service.web.dto.response.FollowerDtoResponse;

import java.util.UUID;

public class mapperFollowEntityToFollowResponse {

    public static FollowerDtoResponse mapToFollowDtoResponse(FollowerDto dto, String avatarUrl){
        return  new FollowerDtoResponse(
                dto.id(),
                dto.userName(),
                avatarUrl);
    }
    public static FollowEntity mapToFollowEntity(UUID userId, UUID id){
        return  new FollowEntity(
                userId,
                id

        );
    }
}
