package com.example.User_Service.mapper;

import com.example.User_Service.domain.User;
import com.example.User_Service.domain.UserEntity;

public class UserMapResponse {
    public UserEntity mapToUserEntity(User user){
        return  new UserEntity(
                user.userName(),
                user.family(),
                user.userName(),
                user.email(),
                user.bio(),
                user.createdAt()
        );
    }
    public User mapToUser(UserEntity userEntity){
        return  new User(userEntity.getName(),
                userEntity.getFamily(),
                userEntity.getUserName(),
                userEntity.getEmail(),
                userEntity.getBio(),
                userEntity.getAvatarUrl(),
                userEntity.getCreatedAt());
    }
}
