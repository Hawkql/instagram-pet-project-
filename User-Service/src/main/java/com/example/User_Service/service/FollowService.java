package com.example.User_Service.service;

import com.example.User_Service.domain.FollowEntity;
import com.example.User_Service.repository.FollowRepository;
import com.example.User_Service.repository.UserRepository;
import com.example.User_Service.web.dto.response.FollowerDto;
import com.example.User_Service.web.dto.response.FollowerDtoResponse;
import com.example.User_Service.web.exception.UserNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class FollowService {

    private  final FollowRepository followRepository;
    private final UserRepository userRepository;
    private  final  S3StorageService storageService;

    public FollowService(FollowRepository followRepository, UserRepository userRepository, S3StorageService storageService) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
        this.storageService = storageService;
    }
    @Transactional
    public void following(UUID userId, UUID id) {
        validateUserAction(userId,id);
        FollowEntity entity= mapToFollowEntity(userId,id);
        if(followRepository.findFollowerIdAndFolloweeId(userId,id)!=null)
            throw new IllegalArgumentException("пользователь уже подписан");
        followRepository.save(entity);

    }
    @Transactional
    public void unFollowing(UUID userId, UUID id) {
        validateUserAction(userId,id);
        if(followRepository.findFollowerIdAndFolloweeId(userId,id)==null)
            throw new IllegalArgumentException("пользователь не подписан");
        followRepository.deleteByFollowerIdAndFolloweeId(userId,id);
    }

    public Slice<FollowerDtoResponse> getFollowers(UUID id, Pageable pageable) {

        Slice<FollowerDto> dtoSlice =followRepository.findFollowers(id,pageable);
        return dtoSlice.map(this::mapToFollowDtoResponse);
    }

    public Slice<FollowerDtoResponse> getFollowing(UUID id, Pageable pageable) {
        Slice<FollowerDto> dtoSlice =followRepository.findFollowing(id,pageable);
        return dtoSlice.map(this::mapToFollowDtoResponse);
    }

    private void validateUserAction(UUID userId,UUID id){
        if(userId.equals(id))
            throw new IllegalArgumentException("пользователь хочется подписаться на себя");

        userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("Not found user by id ="+id));
        userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException("Not found userId by id ="+userId));
    }
    private FollowerDtoResponse mapToFollowDtoResponse(FollowerDto dto){
        return  new FollowerDtoResponse(
                dto.id(),
                dto.userName(),
                storageService.publicUrl(dto.avatarKey()));
    }
    private FollowEntity mapToFollowEntity(UUID userId,UUID id){
        return  new FollowEntity(
                userId,
                id

        );
    }

}
