package com.example.Post_Service.web.dto;

import com.example.Post_Service.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("media/upload-url")
    public ResponseEntity<Void> createPost(){

    }
    @PutMapping()
    public ResponseEntity<Void> putPost(){

    }
    @GetMapping("{id}")
    public ResponseEntity<Void> getPostById(){

    }
    @GetMapping()
    public ResponseEntity<Void> getPostByAuthor(){

    }
    @DeleteMapping("{id}")
    public  ResponseEntity<Void> deletePostById(){

    }
    @PatchMapping("{id}")
    public  ResponseEntity<Void> patchCaption(){

    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likedPost(){

    }
    @DeleteMapping("{id}/like")
    public  ResponseEntity<Void> deleteLikeWithPost(){

    }
    @GetMapping("{id}/")
    public  ResponseEntity<Void> getCommentsWithPost(){

    }
    @PostMapping("{id}/comments")
    public  ResponseEntity<Void> addCommentsWithPost(){

    }
    @DeleteMapping("comments/{id}")
    public  ResponseEntity<Void> deleteCommentsWithPost(){

    }



}
