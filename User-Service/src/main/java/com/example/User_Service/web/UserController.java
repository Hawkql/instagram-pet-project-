package com.example.User_Service.web;

import com.example.User_Service.domain.UserEntity;
import com.example.User_Service.service.AvatarService;
import com.example.User_Service.service.UserService;
import com.example.User_Service.web.dto.*;
import com.example.User_Service.web.dto.avatar.AvatarConfirmRequest;
import com.example.User_Service.web.dto.avatar.AvatarUploadUrlRequest;
import com.example.User_Service.web.dto.avatar.AvatarUploadUrlResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private  final Logger logger = LoggerFactory.getLogger(UserController.class);
    private  final AvatarService avatarService;
    public UserController(UserService userService,AvatarService avatarService) {
        this.avatarService =avatarService;
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<ResponseUserDto> create(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody RequestUserDto requestUserDto){
        UUID id =UUID.fromString(jwt.getSubject());

        var create = userService.create(id,requestUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(create);
    }
    @PutMapping("/{id}")
    public  ResponseEntity<ResponseUserDto> update(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody UpdateUserDto userDto){
        UUID id =UUID.fromString(jwt.getSubject());
        var create = userService.update(id,userDto);
        return ResponseEntity.status(200).body(create);
    }
    @GetMapping("/me")
    public  ResponseEntity<ResponseUserDto> getMe(@AuthenticationPrincipal Jwt jwt ){

        UUID id =UUID.fromString(jwt.getSubject());

        return  ResponseEntity.ok(userService.getMe(id));
    }
    @GetMapping("/{id}")
    public  ResponseEntity<ResponseUserDto> getById(@PathVariable UUID id){
        return  ResponseEntity.ok(userService.getById(id));
    }
    @GetMapping("/search")
    public  ResponseEntity<List<ResponseUserDto>> getAllUsers(
            @RequestParam(name = "name",required = false) String name,
            @RequestParam(name = "family",required = false) String family,
            @RequestParam(name = "userName",required = false) String userName,
            @PageableDefault(page = 10,size = 5)Pageable pageable){
        UserSearchFilter filter =  new UserSearchFilter(name,family,userName);
        List<ResponseUserDto> list =  userService.searchByFilter(filter,pageable);
        return ResponseEntity.ok().body(list);
    }
    @GetMapping("/by-username/{username}")
    public  ResponseEntity<ResponseUserDto> getUserByUserName(@PathVariable String userName ){
        ResponseUserDto entity= userService.getUserById(userName);
        return ResponseEntity.ok(entity);
    }

    @PostMapping("/avatar/upload-url")
    public  ResponseEntity<AvatarUploadUrlResponse> createUploadUrl(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody AvatarUploadUrlRequest avatarUploadUrlRequest){
            UUID id = UUID.fromString(jwt.getSubject());
            return ResponseEntity.ok(avatarService.createAvatarUploadUrl(id,avatarUploadUrlRequest.contentType()));
    }
    @PutMapping("/avatar")
    public  ResponseEntity<ResponseUserDto> confirmAvatar(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody AvatarConfirmRequest avatarConfirmRequest){
        UUID id = UUID.fromString(jwt.getSubject());
        return  ResponseEntity.ok(avatarService.confirmaAvatarUrl(id,avatarConfirmRequest.key()));
    }
    @DeleteMapping("/avatar")
    public  ResponseEntity<Void> deledAvatar(@AuthenticationPrincipal Jwt jwt){
        UUID id = UUID.fromString(jwt.getSubject());
        avatarService.deleteAvatarUrl(id);
        return  ResponseEntity.noContent().build();
    }
    //admin доступ
    @DeleteMapping("/delete/{id}")
    public  ResponseEntity<Void> deledUser(@AuthenticationPrincipal Jwt jwt){

        return  null;
    }
}
