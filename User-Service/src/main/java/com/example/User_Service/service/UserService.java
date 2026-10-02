package com.example.User_Service.service;

import com.example.User_Service.domain.UserEntity;
import com.example.User_Service.mapper.UserEntityMapUser;
import com.example.User_Service.repository.UserRepository;
import com.example.User_Service.web.dto.UpdateUserDto;
import com.example.User_Service.web.dto.UserSearchFilter;
import com.example.User_Service.web.exception.UserAllReadyException;
import com.example.User_Service.web.dto.RequestUserDto;
import com.example.User_Service.web.dto.ResponseUserDto;
import com.example.User_Service.web.exception.UserNotFoundException;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.apache.catalina.mapper.Mapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.example.User_Service.mapper.UserEntityMapUser.mapToResponse;
import static com.example.User_Service.mapper.UserEntityMapUser.mapToUserEntity;

@Service
public class UserService {
    private  final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        return  mapToResponse(user);
    }

    public ResponseUserDto getById(UUID id) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("Not found user by id ="+id));
        return mapToResponse(entity);
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
}
