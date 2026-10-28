package com.example.Post_Service.mapper;

import com.example.Post_Service.domain.CommentsEntity;
import com.example.Post_Service.web.dto.request.ShortUserDto;
import com.example.Post_Service.web.dto.response.CommentResponseUserWithProfile;
import com.example.Post_Service.web.dto.response.CommentToResponse;

import java.util.UUID;

public class MapperCommentEntityToCommentResponse {
    public static CommentsEntity mapCommentToEntity(UUID id,UUID user,String comments){
        return  new CommentsEntity(id,user,comments);
    }
    public static CommentToResponse mapCommentToResponse(CommentsEntity entity){
        return new CommentToResponse(entity.getUser(),entity.getComment());
    }
    public  static  CommentResponseUserWithProfile mapToCommentResponseUserWithProfile(ShortUserDto userDto,CommentsEntity comments){
        return new CommentResponseUserWithProfile(comments.getId(), userDto, comments.getComment(), comments.getCreateAt());
    }
}
