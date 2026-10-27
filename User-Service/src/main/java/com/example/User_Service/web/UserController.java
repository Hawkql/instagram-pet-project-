package com.example.User_Service.web;

import com.example.User_Service.service.AvatarService;
import com.example.User_Service.service.FollowService;
import com.example.User_Service.service.UserService;
import com.example.User_Service.web.dto.avatar.AvatarConfirmRequest;
import com.example.User_Service.web.dto.avatar.AvatarUploadUrlRequest;
import com.example.User_Service.web.dto.avatar.AvatarUploadUrlResponse;
import com.example.User_Service.web.dto.request.RequestUserDto;
import com.example.User_Service.web.dto.request.UpdateUserDto;
import com.example.User_Service.web.dto.request.UserSearchFilter;
import com.example.User_Service.web.dto.response.FollowerDtoResponse;
import com.example.User_Service.web.dto.response.ResponseOtherUserDto;
import com.example.User_Service.web.dto.response.ResponseUserDto;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private  final Logger logger = LoggerFactory.getLogger(UserController.class);
    private  final AvatarService avatarService;
    private  final FollowService followService;
    public UserController(UserService userService,
                          AvatarService avatarService,
                          FollowService followService) {
        this.avatarService =avatarService;
        this.userService = userService;
        this.followService=followService;
    }

    @PostMapping()
    public ResponseEntity<ResponseUserDto> create(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody RequestUserDto requestUserDto){
        UUID id =UUID.fromString(jwt.getSubject());
        logger.info("Start method create for user ="+id);
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
    public  ResponseEntity<?> getById(@AuthenticationPrincipal Jwt jwt ,@PathVariable UUID id){
        UUID idJwt =UUID.fromString(jwt.getSubject());
        if(idJwt.equals(id))
            return  ResponseEntity.ok(userService.getMe(idJwt));
        else
            return  ResponseEntity.ok(userService.getById(id));
    }
    @GetMapping("/search")
    public  ResponseEntity<List<ResponseUserDto>> getAllUsers(
            @RequestParam(name = "name",required = false) String name,
            @RequestParam(name = "family",required = false) String family,
            @RequestParam(name = "userName",required = false) String userName,
            @ParameterObject @PageableDefault(page = 10,size = 5)Pageable pageable){
        UserSearchFilter filter =  new UserSearchFilter(name,family,userName);
        List<ResponseUserDto> list =  userService.searchByFilter(filter,pageable);
        return ResponseEntity.ok().body(list);
    }
    @GetMapping("/by-username/{username}")
    public  ResponseEntity<ResponseUserDto> getUserByUserName(@PathVariable("username") String userName ){
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
        return  ResponseEntity.ok(avatarService.confirmAvatar(id,avatarConfirmRequest.key()));
    }
    @DeleteMapping("/avatar")
    public  ResponseEntity<Void> deleteAvatar(@AuthenticationPrincipal Jwt jwt){
        UUID id = UUID.fromString(jwt.getSubject());
        avatarService.deleteAvatar(id);
        return  ResponseEntity.noContent().build();
    }
    @DeleteMapping("/delete")
    public  ResponseEntity<Void> deleteUser(@AuthenticationPrincipal Jwt jwt){
        UUID id = UUID.fromString(jwt.getSubject());
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/{id}/follow")
    public ResponseEntity<Void> followingByUser(@AuthenticationPrincipal Jwt jwt,
                                             @PathVariable UUID id){
        UUID userId = UUID.fromString(jwt.getSubject());
        followService.following(userId,id);
        return  ResponseEntity.ok().build();
    }
    @DeleteMapping("/{id}/follow")
    public  ResponseEntity<Void> unFollowed(@AuthenticationPrincipal Jwt jwt,
                                            @PathVariable UUID id){
        UUID userId = UUID.fromString(jwt.getSubject());
        followService.unFollowing(userId,id);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{id}/followers")
    public ResponseEntity<Slice<FollowerDtoResponse>> getFollowers(@PathVariable UUID id,
                                                            @ParameterObject @PageableDefault(page = 10,size = 5) Pageable pageable){

        return ResponseEntity.ok().body(followService.getFollowers(id,pageable));

    }
    @GetMapping("/{id}/following")
    public  ResponseEntity<Slice<FollowerDtoResponse>> getFollowing(@PathVariable UUID id,
                                              @ParameterObject @PageableDefault(page = 10,size = 5) Pageable pageable){
        return ResponseEntity.ok().body(followService.getFollowing(id,pageable));
    }

    //сообщи мне про этот метод .Я его не писал внутри ещё чтобы не забыть дописать
    @GetMapping("/batch")
    public List<ResponseOtherUserDto> getBatch(@RequestParam Set<UUID> ids) {
        if (ids.size() > 100) throw new IllegalArgumentException("было запрошенно больше данных пользователей чем можно было ");
        return userService.findAllShort(ids);
    }
}
