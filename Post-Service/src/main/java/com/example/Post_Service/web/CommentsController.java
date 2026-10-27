package com.example.Post_Service.web;

import com.example.Post_Service.service.PostService;
import com.example.Post_Service.web.dto.request.DeleteCommentRequest;
import com.example.Post_Service.web.dto.request.UpdateCommentsRequest;
import com.example.Post_Service.web.dto.response.CommentResponseUserWithProfile;
import com.example.Post_Service.web.dto.response.CommentToResponse;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/comments")
public class CommentsController {
    private final PostService postService;

    public CommentsController(PostService postService) {
        this.postService = postService;
    }


    @DeleteMapping("/deleted")
    public ResponseEntity<Void> deleteCommentsWithPost(@AuthenticationPrincipal Jwt jwt, @RequestBody DeleteCommentRequest request){
        UUID userId = UUID.fromString(jwt.getSubject());
        postService.deleteComments(userId,request);
        return ResponseEntity.ok().build();
    }
    @GetMapping("{id}/")
    public  ResponseEntity<Slice<CommentResponseUserWithProfile>> getCommentsWithPost(@AuthenticationPrincipal Jwt jwt ,
                                                                                     @PageableDefault(size = 10,page = 5) PageableDefault page,
                                                                                     @PathVariable("id") UUID postId){
        UUID userId = UUID.fromString(jwt.getSubject());

        return  ResponseEntity.ok().body( postService.getComments(postId,page));
    }
    @PutMapping("{id}")
    public  ResponseEntity<CommentToResponse> updateCommentsWithPost(@AuthenticationPrincipal Jwt jwt, @RequestBody UpdateCommentsRequest request){
        UUID userId = UUID.fromString(jwt.getSubject());

        return ResponseEntity.ok().body(postService.updateComments(userId,request));
    }
}
