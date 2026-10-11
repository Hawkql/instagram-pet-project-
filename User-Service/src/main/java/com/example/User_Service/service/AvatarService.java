package com.example.User_Service.service;

import com.example.User_Service.domain.S3ContentType;
import com.example.User_Service.domain.UserEntity;
import com.example.User_Service.repository.UserRepository;
import com.example.User_Service.web.dto.response.ResponseUserDto;
import com.example.User_Service.web.dto.avatar.AvatarUploadUrlResponse;
import com.example.User_Service.web.exception.ObjectNotFoundException;
import com.example.User_Service.web.exception.UserNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.example.User_Service.mapper.UserEntityMapUser.mapToResponse;

@Service
public class AvatarService {

    private final UserRepository userRepository;
    private final S3StorageService storageService;

    public AvatarService(UserRepository userRepository, S3StorageService storageService) {
        this.userRepository = userRepository;
        this.storageService = storageService;
    }

    public AvatarUploadUrlResponse createAvatarUploadUrl(UUID userId, String contentType) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Профиль не найден, id = " + userId);
        }
        String ext = extensionFor(contentType);
        String key = storageService.generateKey(userId, ext);
        String url = storageService.putUrl(key, contentType);
        return new AvatarUploadUrlResponse(url, key);
    }


    @Transactional
    public ResponseUserDto confirmAvatar(UUID userId, String key) {
        validateOwnership(userId, key);

        if (!storageService.exists(key)) {
            throw new ObjectNotFoundException("Объект не найден в хранилище: " + key);
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Профиль не найден, id = " + userId));

        user.setAvatarUrl(key);

        return mapToResponse(user);
    }

    @Transactional
    public ResponseUserDto deleteAvatar(UUID userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Профиль не найден, id = " + userId));

        user.setAvatarUrl(null);

        return mapToResponse(user);
    }


    private void validateOwnership(UUID userId, String key) {
        if (!key.startsWith("avatars/" + userId + "/")) {
            throw new AccessDeniedException("Ключ не принадлежит пользователю");
        }
    }

    private static String extensionFor(String contentType) {
        return S3ContentType.fromMimeType(contentType).getExtension();
    }
}