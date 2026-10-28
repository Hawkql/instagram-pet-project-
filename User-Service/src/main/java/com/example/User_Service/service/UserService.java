package com.example.User_Service.service;

import com.example.User_Service.domain.UserEntity;
import com.example.User_Service.mapper.UserEntityMapUser;
import com.example.User_Service.repository.UserRepository;
import com.example.User_Service.web.dto.request.UpdateUserDto;
import com.example.User_Service.web.dto.request.UserSearchFilter;
import com.example.User_Service.web.dto.response.ResponseOtherUserDto;
import com.example.User_Service.web.dto.response.UserResponseToComments;
import com.example.User_Service.web.exception.UserAllReadyException;
import com.example.User_Service.web.dto.request.RequestUserDto;
import com.example.User_Service.web.dto.response.ResponseUserDto;
import com.example.User_Service.web.exception.UserNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.example.User_Service.mapper.UserEntityMapUser.*;

@Service
public class UserService {
    private  final UserRepository userRepository;
    private  final Logger logger = LoggerFactory.getLogger(UserService.class);
    private  final  S3StorageService storageService;
    public UserService(UserRepository userRepository, S3StorageService storageService) {
        this.userRepository = userRepository;
        this.storageService = storageService;
    }

    @Transactional
    public ResponseUserDto create(UUID id,RequestUserDto createUserDto) {
        if(userRepository.existsByEmail(createUserDto.email())){
            throw new UserAllReadyException(createUserDto.email());
        }
        if(userRepository.existsByUserName(createUserDto.userName())){
            throw new UserAllReadyException(createUserDto.userName());
        }

        UserEntity user = mapToUserEntity(id,createUserDto);

        userRepository.save(user);
        logger.info("User save to database id"+id);
        return  mapToResponse(user);
    }

    public ResponseOtherUserDto getById(UUID id) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("Not found user by id ="+id));
        return mapToResponseOtherDto(entity);
    }

    public ResponseUserDto getMe(UUID id) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Not found user by id ="+id));
        return mapToResponse(entity);
    }

    public ResponseUserDto getUserById(String userName) {
        UserEntity entity = userRepository.findByUserName(userName);
        return mapToResponse(entity);
    }
    @Transactional
    public ResponseUserDto update(UUID id, @Valid UpdateUserDto userDto) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("Not found user by id ="+id));
        entity.update(userDto);
        userRepository.save(entity);
        return mapToResponse(entity);
    }

    public List<ResponseUserDto> searchByFilter(UserSearchFilter filter, Pageable pageable) {
        return userRepository.searchAllByFilter(filter.name(),filter.family(),filter.userName(),pageable)
                .stream().map(UserEntityMapUser::mapToResponse).toList();

    }

    @Transactional
    public void deleteUser(UUID id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("Not found user by id ="+id));
        userRepository.delete(user);
    }

    public List<UserResponseToComments> findAllShort(Set<UUID> ids) {
        List<UserEntity> entityList = userRepository.findAllById(ids);
        return entityList.stream().map(user->{
                    String publicAvatarUrl=storageService.publicUrl(user.getAvatarUrl());
                    return  mapToUserResponseToComments(user,publicAvatarUrl);
                })
                .toList();
    }
}
