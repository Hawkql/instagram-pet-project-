package com.example.User_Service.service;

import com.example.User_Service.domain.UserEntity;
import com.example.User_Service.repository.UserRepository;
import com.example.User_Service.web.dto.ResponseUserDto;
import com.example.User_Service.web.dto.avatar.AvatarConfirmRequest;
import com.example.User_Service.web.dto.avatar.AvatarUploadUrlRequest;
import com.example.User_Service.web.dto.avatar.AvatarUploadUrlResponse;
import com.example.User_Service.web.exception.UserNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AvatarService {
    private final UserRepository userRepository;
    private final S3StorageService storageService;

    public AvatarService(UserRepository userRepository,S3StorageService storageService) {
        this.userRepository = userRepository;
        this.storageService=storageService;
    }
    @Transactional
    public ResponseUserDto confirmaAvatarUrl(
            UUID id,
            String avatarConfirmRequest) {


    }

    public Void deleteAvatarUrl(UUID id) {


    }

    public AvatarUploadUrlResponse createAvatarUploadUrl(
            UUID id,
            String contentType) {
        userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("Not found user by id ="+id));
        String ext = getExtensionFor(contentType);
        String key = storageService.generateKey(id,ext);
        String url = storageService.putUrl(key,contentType);
        return new AvatarUploadUrlResponse(url,key);
    }

    private static String getExtensionFor(String mimeType) {
        if (mimeType == null) {
            throw new IllegalArgumentException("MIME-тип не может быть null");
        }

        return switch (mimeType) {
            case "image/jpeg" -> "jpg";
            case "image/png"  -> "png";
            default -> throw new IllegalArgumentException("Неподдерживаемый MIME-тип для S3: " + mimeType);
        };
    }
}
