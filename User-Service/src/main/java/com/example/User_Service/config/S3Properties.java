package com.example.User_Service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
@ConfigurationProperties(prefix = "s3")
public record S3Properties(String endpoint,
                           String publicUrl,
                           String accessKey,
                           String secretKey,
                           String region,
                           String bucket,
                           Duration presignTtl) {
}
