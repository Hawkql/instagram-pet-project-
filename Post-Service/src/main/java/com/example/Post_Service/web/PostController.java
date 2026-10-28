package com.example.Post_Service.web;

import com.example.Post_Service.service.PostService;
import com.example.Post_Service.web.dto.postMedia.PostUploadUrl;
import com.example.Post_Service.web.dto.postMedia.PostUploadUrlResponse;
import com.example.Post_Service.web.dto.request.CommentsRequest;
import com.example.Post_Service.web.dto.request.PostRequest;
import com.example.Post_Service.web.dto.request.UpdatePostCaptionRequest;
import com.example.Post_Service.web.dto.response.CommentToResponse;
import com.example.Post_Service.web.dto.response.LikeResponse;
import com.example.Post_Service.web.dto.response.PostResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<PostUploadUrlResponse> createPostUploadUrl(@AuthenticationPrincipal Jwt jwt,
                                                                     @Valid @RequestBody PostUploadUrl request){
        UUID id = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok().body(postService.createPostUploadUrlPost(id,request));
    }
    @PostMapping("/create")
    public ResponseEntity<PostResponse> putPost(@AuthenticationPrincipal Jwt jwt,
                                                @Valid @RequestBody PostRequest request){
        UUID id = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok().body(postService.confirmPost(id,request));
    }
    @GetMapping("{id}")
    public ResponseEntity<PostResponse> getPostById(@AuthenticationPrincipal Jwt jwt,
                                                    @PathVariable("id") UUID postId){
        UUID id = UUID.fromString(jwt.getSubject());

        return ResponseEntity.ok().body(postService.getPostById(id,postId));
    }
    @GetMapping("/author/{id}")
    public ResponseEntity<Slice<PostResponse>> getPostByAuthor(@AuthenticationPrincipal Jwt jwt,
                                                               @PathVariable UUID authorId,
                                                 @PageableDefault(size = 5,page = 0)Pageable page){
        UUID id = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(postService.getPostByAuthor(id,authorId,page));
    }
    @DeleteMapping("{id}")
    public  ResponseEntity<Void> deletePostById(@AuthenticationPrincipal Jwt jwt,
                                                @PathVariable("id") UUID idPost){
        UUID id = UUID.fromString(jwt.getSubject());
        postService.deletePostById(id,idPost);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("{id}")
    public  ResponseEntity<PostResponse> patchCaption(@AuthenticationPrincipal Jwt jwt,
                                                      @PathVariable("id") UUID idPost,
                                              @Valid @RequestBody UpdatePostCaptionRequest request){
        UUID id = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok().body(postService.patchCaptionById(id,idPost,request.caption()));
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<LikeResponse> likedPost(@AuthenticationPrincipal Jwt jwt, @PathVariable("id") UUID idPost){
        UUID id = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok().body(postService.likePost(id,idPost));
    }
    // добавить мягкое удаление
    @DeleteMapping("{id}/like")
    public  ResponseEntity<LikeResponse> deleteLikeWithPost(@AuthenticationPrincipal Jwt jwt, @PathVariable("id") UUID idPost){
        UUID id = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(postService.unLikePost(id,idPost));
    }

    @PostMapping("{id}/comments")
    public  ResponseEntity<CommentToResponse> addCommentsWithPost(@AuthenticationPrincipal Jwt jwt, @PathVariable("id") UUID idPost,
                                                                  @Valid @RequestBody CommentsRequest request){
        UUID id = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok().body(postService.addComment(id,idPost,request.comments()));
    }

}
