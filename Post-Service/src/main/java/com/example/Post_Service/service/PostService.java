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
import com.example.Post_Service.web.dto.response.PostResponse;
import com.example.Post_Service.web.exception.*;
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

        postRepository.save(entity);
        String publicUrl = storageService.publicUrl(entity.getMedia());
        return  mapToResponse(entity,publicUrl);
    }

    public PostResponse getPostById(UUID id) {
         PostEntity entity= postRepository.findById(id).
                 orElseThrow(()->new PostNotFoundException("Not found post by id ="+id));

         return mapToResponse(entity,mapUrlToPublic(entity.getMedia()));
    }

    public Slice<PostResponse> getPostByAuthor(UUID currenIdUser,UUID authorId, Pageable page) {
        //добпвить пагинацию
        Slice<PostEntity> post=  postRepository.findByAuthorId(authorId,page);
        Set<UUID> postIds = post.stream()
                .map(PostEntity::getAuthorId)
                .collect(Collectors.toSet());
       Set<UUID> likedPost = likePostRepository.findLikedPostIds(currenIdUser,authorId);

        return  post.stream()
                .map(entity -> mapToResponse(
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

    }
    @Transactional
    public PostResponse patchCaptionById(UUID authorId, UUID idPost,String caption) {
            PostEntity entity= postRepository.findByIdAndAuthorId(authorId,idPost);
            entity.setCaption(caption);
            return mapToResponse(entity,mapUrlToPublic(entity.getMedia()));
    }
    @Transactional
    public void likePost(UUID id, UUID postId) {
        likePostRepository.existsByIdAndIdPost(id,postId);
        if(likePostRepository.existsByIdAndIdPost(id,postId)!=null)
            throw new  PostAlreadyLikedException("Пост уже лайкнут ");
        LikePostEntity like =new LikePostEntity(postId,id);
        likePostRepository.save(like);
        postRepository.incrementLikes(postId);
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
    public void unlikePost(UUID id, UUID postId) {
        LikePostEntity like=likePostRepository.existsByIdAndIdPost(id,postId);
        if(like==null)
            throw new  PostNotLikedException("Пост ещё не лайкнут ");
        likePostRepository.delete(like);
        postRepository.decrementLikesCount(postId);

    }
    private  String mapUrlToPublic(String key){
        return storageService.publicUrl(key);
    }
    @Transactional
    public void deleteComments(UUID userId, DeleteCommentRequest request) {
        PostEntity entity=  postRepository.findById(request.postId())
                .orElseThrow(()->new PostNotFoundException("Not found post by id ="+request.postId()));
        if(!entity.getAuthorId().equals(userId))
            throw  new UserDoesntHavePermissionsException("Пользовательid ="+userId+ " не может удалить чужой пост"+request.postId());
        postRepository.delete(entity);
    }

    public Slice<CommentResponseUserWithProfile> getComments( UUID postId, PageableDefault page) {
        Slice<CommentsEntity>comments  =commentRepository.findByIdPost(postId,page);
        Set<UUID> authorIds = comments.stream().map(CommentsEntity::getUser).collect(Collectors.toSet());
        List<ShortUserDto> user = userClient.findByIds(authorIds);
        Map<UUID,ShortUserDto> userWithComments = user.stream().collect(Collectors.toMap(ShortUserDto::id, Function.identity()));

        return comments.map(c -> mapToCommentResponseUserWithProfile(
                userWithComments.get(c.getUser()), c));
    }
    @Transactional
    public CommentToResponse updateComments(UUID userId, UpdateCommentsRequest request) {
        PostEntity entity=  postRepository.findById(request.postId())
                .orElseThrow(()->new PostNotFoundException("Not found post by id ="+request.postId()));
        if(!entity.getAuthorId().equals(userId))
            throw  new  UserDoesntHavePermissionsException("Пользователь id ="+userId+ "не может изменить чужой пост"+request.postId());
        CommentsEntity comments = commentRepository.findById(request.commentsId())
                .orElseThrow(()->new CommentsNotFoundException("Not found comments by id ="+request.commentsId()));
        if(comments.getUser().equals(request.commentsId()))
            throw new UserDoesntHavePermissionsException("Пользователь id ="+userId+ "не может изменить чужой комментарий"+request.commentsId());
        comments.setComment(request.comments());
        commentRepository.save(comments);
        return  mapCommentToResponse(comments);
    }

}
