package com.example.Post_Service.service;

import com.example.Post_Service.adapter.UserClient;
import com.example.Post_Service.domain.CommentsEntity;
import com.example.Post_Service.domain.LikePostEntity;
import com.example.Post_Service.domain.PostEntity;

import com.example.Post_Service.repository.CommentRepository;
import com.example.Post_Service.repository.LikePostRepository;
import com.example.Post_Service.repository.PostRepository;
import com.example.Post_Service.web.dto.postMedia.PostUploadUrl;
import com.example.Post_Service.web.dto.postMedia.PostUploadUrlResponse;
import com.example.Post_Service.web.dto.request.DeleteCommentRequest;
import com.example.Post_Service.web.dto.request.PostRequest;
import com.example.Post_Service.web.dto.request.ShortUserDto;
import com.example.Post_Service.web.dto.request.UpdateCommentsRequest;
import com.example.Post_Service.web.dto.response.CommentResponseUserWithProfile;
import com.example.Post_Service.web.dto.response.CommentToResponse;
import com.example.Post_Service.web.dto.response.LikeResponse;
import com.example.Post_Service.web.dto.response.PostResponse;
import com.example.Post_Service.web.exception.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.example.Post_Service.mapper.MapperCommentEntityToCommentResponse.mapCommentToResponse;
import static com.example.Post_Service.mapper.MapperCommentEntityToCommentResponse.mapToCommentResponseUserWithProfile;

import static com.example.Post_Service.mapper.MapperPostEntityToPostResponse.mapToEntity;
import static com.example.Post_Service.mapper.MapperPostEntityToPostResponse.mapToResponse;

@Service
public class PostService {
    private  final PostRepository postRepository;
    private final  MediaService mediaService;
    private  final  S3StorageService storageService;
    private  final LikePostRepository likePostRepository;
    private final CommentRepository commentRepository;
    private  final UserClient userClient;
    public PostService(PostRepository postRepository, MediaService mediaService, S3StorageService storageService, LikePostRepository likePostRepository, CommentRepository commentRepository, UserClient userClient) {
        this.postRepository = postRepository;
        this.mediaService = mediaService;
        this.storageService = storageService;
        this.likePostRepository = likePostRepository;
        this.commentRepository = commentRepository;
        this.userClient = userClient;
    }
    public PostUploadUrlResponse createPostUploadUrlPost(UUID authorId, PostUploadUrl request) {
        return mediaService.createPostUploadUrl(authorId,request.contentType());
    }
    @Transactional
    public PostResponse confirmPost(UUID authorId, PostRequest request) {
        mediaService.confirmPost(authorId,request.key());
        PostEntity entity = mapToEntity(authorId,request);

        postRepository.saveAndFlush(entity);
        String publicUrl = storageService.publicUrl(entity.getMedia());
        return  mapToResponse(entity,false,publicUrl);
        //false передаётся так как создание
    }

    public PostResponse getPostById(UUID userId,UUID postId) {
         PostEntity entity= postRepository.findById(postId).
                 orElseThrow(()->new PostNotFoundException("Not found post by id ="+postId));

        boolean likedByMe = userId != null
                && likePostRepository.existsByPostIdAndLikedAuthor(postId, userId);
         return mapToResponse(entity,likedByMe,mapUrlToPublic(entity.getMedia()));
    }

    public Slice<PostResponse> getPostByAuthor(UUID currenIdUser,UUID authorId, Pageable page) {

        Slice<PostEntity> post=  postRepository.findByAuthorId(authorId,page);
        Set<UUID> postIds = post.stream()
                .map(PostEntity::getId)
                .collect(Collectors.toSet());
       Set<UUID> likedPost = likePostRepository.findLikedPostIds(currenIdUser,postIds);

        return  post.map(entity -> mapToResponse(
                        entity,
                        likedPost.contains(entity.getAuthorId()),
                        mapUrlToPublic(entity.getMedia())))
                ;
    }
    @Transactional
    public void deletePostById(UUID authorId, UUID idPost) {
        PostEntity entity =  postRepository.findById(idPost)
                .orElseThrow(()->new PostNotFoundException("Not found post by id ="+idPost));;
        if(!entity.getAuthorId().equals(authorId))
            throw  new PostOwnerMismatchException("пользователь не имеет права на удаление"+idPost);
        postRepository.delete(entity);
        mediaService.deletePost(authorId,entity.getMedia());
    }
    @Transactional
    public PostResponse patchCaptionById(UUID authorId, UUID postId,String caption) {
            PostEntity entity= postRepository.findByIdAndAuthorId(authorId,postId);
            entity.setCaption(caption);
            boolean likedByMe = authorId != null
                        && likePostRepository.existsByPostIdAndLikedAuthor(postId, authorId);
            return mapToResponse(entity,likedByMe,mapUrlToPublic(entity.getMedia()));
    }
    @Transactional
    public LikeResponse likePost(UUID userId, UUID postId) {

        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Not found post by id = " + postId));

        if (likePostRepository.existsByPostIdAndLikedAuthor(postId, userId)) {
            return new LikeResponse(post.getLikesCount(), true);
        }

        try {
            likePostRepository.save(new LikePostEntity(postId, userId));
            postRepository.incrementLikes(postId);
            return new LikeResponse(post.getLikesCount() + 1, true);
        } catch (DataIntegrityViolationException e) {
            return new LikeResponse(post.getLikesCount(), true);
        }
    }
    @Transactional
    public CommentToResponse addComment(UUID id, UUID postId, String comments) {
        PostEntity postEntity= postRepository.findById(postId)
                .orElseThrow(()->new PostNotFoundException("Not found post by id ="+id));
        CommentsEntity commentsEntity=new CommentsEntity(postId,id,comments);
        commentRepository.save(commentsEntity);
        postRepository.incrementCommentsCount(postId);
        return mapCommentToResponse(commentsEntity);
    }
    @Transactional
    public LikeResponse unLikePost(UUID userId, UUID postId) {

        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Not found post by id = " + postId));

        int deleted = likePostRepository.deleteByPostIdAndLikedAuthor(postId, userId);

        if (deleted == 0) {
            return new LikeResponse(post.getLikesCount(), false);
        }

        postRepository.decrementLikesCount(postId);
        return new LikeResponse(post.getLikesCount() - 1, false);
    }
    private  String mapUrlToPublic(String key){
        return storageService.publicUrl(key);
    }
    @Transactional
    public void deleteComments(UUID userId, DeleteCommentRequest request) {
        if(request.commentsId()==null)
            return;
        PostEntity entity=  postRepository.findById(request.postId())
                .orElseThrow(()->new PostNotFoundException("Not found post by id ="+request.postId()));
        if(!entity.getAuthorId().equals(userId))
            throw  new UserDoesntHavePermissionsException("Пользовательid ="+userId+ " не может удалить чужой пост"+request.postId());
        CommentsEntity comments= commentRepository.findById(request.commentsId())
                .orElseThrow(()->new PostNotFoundException("Not found comments by id ="+request.commentsId()));
        commentRepository.delete(comments);
        postRepository.decrementCommentsCount(request.postId());
    }

    public Slice<CommentResponseUserWithProfile> getComments( UUID postId, Pageable page) {
        Slice<CommentsEntity>comments  =commentRepository.findByIdPost(postId,page);
        Set<UUID> authorIds = comments.stream().map(CommentsEntity::getUser).collect(Collectors.toSet());
        List<ShortUserDto> user = userClient.findByIds(authorIds);
        Map<UUID,ShortUserDto> userWithComments = user.stream().collect(Collectors.toMap(ShortUserDto::id, Function.identity()));

        return comments.map(c -> mapToCommentResponseUserWithProfile(
                userWithComments.get(c.getUser()), c));
    }
    @Transactional
    public CommentToResponse updateComments(UUID userId, UpdateCommentsRequest request) {
        CommentsEntity comments = commentRepository.findById(request.commentsId())
                .orElseThrow(()->new CommentsNotFoundException("Not found comments by id ="+request.commentsId()));
        if(!comments.getUser().equals(userId))
            throw new UserDoesntHavePermissionsException("Пользователь id ="+userId+ "не может изменить чужой комментарий"+request.commentsId());
        comments.setComment(request.comments());
        commentRepository.save(comments);
        return  mapCommentToResponse(comments);
    }

}
