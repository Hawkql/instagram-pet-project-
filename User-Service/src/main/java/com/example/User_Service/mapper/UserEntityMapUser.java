package com.example.User_Service.mapper;

import com.example.User_Service.domain.UserEntity;
import com.example.User_Service.web.dto.request.RequestUserDto;
import com.example.User_Service.web.dto.response.ResponseOtherUserDto;
import com.example.User_Service.web.dto.response.ResponseUserDto;

import java.util.UUID;

public class UserEntityMapUser {
    public static UserEntity mapToUserEntity(UUID id, RequestUserDto user){
        return  new UserEntity(
                id,
                user.name(),
                user.family(),
                user.userName(),
                user.email(),
                user.bio()
        );
    }
    public static ResponseUserDto mapToResponse(UserEntity userEntity){
        return  new ResponseUserDto(
                userEntity.getName(),
                userEntity.getFamily(),
                userEntity.getUserName(),
                userEntity.getEmail(),
                userEntity.getBio()
        );

    }
    public  static ResponseOtherUserDto mapToResponseOtherDto(UserEntity userEntity){
        return  new ResponseOtherUserDto(
                userEntity.getName(),
                userEntity.getFamily(),
                userEntity.getUserName(),
                userEntity.getBio(),
                userEntity.getAvatarUrl()
        );
    }


}
