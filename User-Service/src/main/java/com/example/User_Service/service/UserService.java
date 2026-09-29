package com.example.User_Service.service;

import com.example.User_Service.domain.User;
import com.example.User_Service.repository.UserRepository;
import com.example.User_Service.web.dto.CreateUserDto;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private  final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User create(@Valid CreateUserDto createUserDto) {
        if(userRepository.fi)
    }

}
