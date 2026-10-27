package com.example.Post_Service.service;

import com.example.Post_Service.domain.S3ContentType;
import com.example.Post_Service.web.dto.postMedia.PostUploadUrlResponse;
import com.example.Post_Service.web.exception.ObjectNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
@Service
public class MediaService {

    private final S3StorageService storageService;

    public MediaService(S3StorageService storageService) {
        this.storageService = storageService;
    }

    public PostUploadUrlResponse createPostUploadUrl(UUID authorId, String contentType) {

        String ext = extensionFor(contentType);
        String key = storageService.generateKey(authorId, ext);
        String url = storageService.putUrl(key, contentType);
        return new PostUploadUrlResponse(url, key);
    }



    public void confirmPost(UUID authorId, String key) {
        validateOwnership(authorId, key);

        if (!storageService.exists(key)) {
            throw new ObjectNotFoundException("Объект не найден в хранилище: {}" + key);
        }

    }


    public void deleteAvatar(UUID userId,String key ) {
        validateOwnership(userId,key);
    }


    private void validateOwnership(UUID userId, String key) {
        if (!key.startsWith("post/" + userId + "/")) {
            throw new AccessDeniedException("Ключ не принадлежит пользователю");
        }
    }

    private static String extensionFor(String contentType) {
        return S3ContentType.fromMimeType(contentType).getExtension();
    }
}
