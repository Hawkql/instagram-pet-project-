package com.example.User_Service.domain;

import lombok.Getter;

import java.util.Arrays;
@Getter
public enum S3ContentType {
    JPEG("image/jpeg", "jpg"),
    PNG("image/png", "png"),
    WEBP("image/webp", "webp");

    private final String mimeType;
    private final String extension;

    S3ContentType(String mimeType, String extension) {
        this.mimeType = mimeType;
        this.extension = extension;
    }


    public static S3ContentType fromMimeType(String mimeType) {
        if (mimeType == null) {
            throw new IllegalArgumentException("Content-Type не может быть null");
        }
        return Arrays.stream(values())
                .filter(contentType -> contentType.mimeType.equalsIgnoreCase(mimeType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Неподдерживаемый тип файла: " + mimeType));
    }
}
