package com.example.User_Service.web;

import com.example.User_Service.service.UserService;
import com.example.User_Service.web.dto.CreateUserDto;
import com.example.User_Service.web.dto.ResponseUserDto;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private  final Logger logger = LoggerFactory.getLogger(UserController.class);
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<ResponseUserDto> create(@Valid @RequestBody CreateUserDto createUserDto){
        var create = userService.create(createUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(create);
    }


}
