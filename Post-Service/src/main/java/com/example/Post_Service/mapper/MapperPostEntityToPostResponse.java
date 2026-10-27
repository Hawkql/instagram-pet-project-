package com.example.Post_Service.mapper;

import com.example.Post_Service.domain.PostEntity;
import com.example.Post_Service.web.dto.request.PostRequest;
import com.example.Post_Service.web.dto.response.PostResponse;

import java.util.UUID;

public class MapperPostEntityToPostResponse {
    public static PostResponse mapToResponse(PostEntity entity,Boolean likedPost ,String url){
        return  new PostResponse(
                entity.getId(),
                entity.getAuthorId(),
                entity.getCaption(),
                url,entity.getLikesCount(),
                entity.getCommentsCount(),
                likedPost,
                entity.getCreatedAt()
                );
    }
    public static PostEntity mapToEntity(UUID id, PostRequest request){
        return  new PostEntity(
                id,
                request.caption(),
                request.key());
    }
}
