package com.example.Post_Service.web;

import com.example.Post_Service.service.PostService;
import com.example.Post_Service.web.dto.postMedia.PostUploadUrl;
import com.example.Post_Service.web.dto.postMedia.PostUploadUrlResponse;
import com.example.Post_Service.web.dto.request.CommentsRequest;
import com.example.Post_Service.web.dto.request.PostRequest;
import com.example.Post_Service.web.dto.request.UpdateRequest;
import com.example.Post_Service.web.dto.response.PostResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;


    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("media/upload-url")
    public ResponseEntity<PostUploadUrlResponse> createPostUploadUrl(@AuthenticationPrincipal Jwt jwt, @RequestBody PostUploadUrl request){
        UUID id = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok().body(postService.createPostUploadUrlPost(id,request));
    }
    @PostMapping("/create")
    public ResponseEntity<PostResponse> putPost(@AuthenticationPrincipal Jwt jwt,
                                                @RequestBody PostRequest request){
        UUID id = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok().body(postService.confirmPost(id,request));
    }
    @GetMapping("{id}")
    public ResponseEntity<> getPostById(@RequestParam UUID id ){
        postService.getPostById(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/author/{id}")
    public ResponseEntity<Void> getPostByAuthor(@RequestParam UUID authorId ){
        postService.getPostByAuthor(authorId);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("{id}")
    public  ResponseEntity<Void> deletePostById(@AuthenticationPrincipal Jwt jwt,
                                                @RequestParam UUID idPost){
        UUID id = UUID.fromString(jwt.getSubject());
        postService.deletePostById(id,idPost);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("{id}")
    public  ResponseEntity<Void> patchCaption(@AuthenticationPrincipal Jwt jwt,
                                              @RequestParam UUID idPost,
                                              @RequestBody String caption ){
        UUID id = UUID.fromString(jwt.getSubject());
        postService.patchCaptionById(id,idPost,caption);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likedPost(@AuthenticationPrincipal Jwt jwt, @PathVariable("id") UUID idPost){
        UUID id = UUID.fromString(jwt.getSubject());
        postService.likePost(id,idPost);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("{id}/like")
    public  ResponseEntity<Void> deleteLikeWithPost(@AuthenticationPrincipal Jwt jwt, @PathVariable("id") UUID idPost){
        UUID id = UUID.fromString(jwt.getSubject());
        postService.unlikePost(id,idPost);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("{id}/comments")
    public  ResponseEntity<Void> addCommentsWithPost(@AuthenticationPrincipal Jwt jwt, @PathVariable("id") UUID idPost,
    @RequestBody CommentsRequest request){
        UUID id = UUID.fromString(jwt.getSubject());
        postService.addComment(id,idPost,request.comments());
        return ResponseEntity.ok().build();
    }

}
