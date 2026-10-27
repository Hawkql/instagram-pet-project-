package com.example.Post_Service.service;

import com.example.Post_Service.config.S3Properties;
import com.example.Post_Service.domain.PermittedExtensions;
import com.example.Post_Service.web.exception.UnsupportedFileExtensionException;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;

@Service
public class S3StorageService {
    private  final S3Presigner s3Presigner;
    private  final S3Properties s3Properties;
    private  final S3Client s3Client;

    public S3StorageService(S3Presigner s3Presigner,S3Properties s3Properties,S3Client s3Client) {
        this.s3Properties=s3Properties;
        this.s3Presigner = s3Presigner;
        this.s3Client = s3Client;
    }

    public String generateKey(UUID userId, String ext){
        if(isNotPermitted(ext))
            throw  new UnsupportedFileExtensionException("Недопустимое расширение: " + ext);
        return  "post/"+userId.toString()+"/"+UUID.randomUUID()+"."+ext;
    }

    public String publicUrl(String key){
        if(key ==null) return null;
        return  s3Properties.publicUrl()+"/"+s3Properties.bucket()+"/"+key;
    }
    public String putUrl(String key, String contentType){
        var presignedRequest= s3Presigner.presignPutObject(PutObjectPresignRequest.builder()
                .putObjectRequest(
                        b-> b.bucket(s3Properties.bucket())
                                .key(key)
                                .contentType(contentType))
                .signatureDuration(Duration.ofMinutes(5))
                .build());

        return presignedRequest.url().toString();
    }
    public  void delete(String key){
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(s3Properties.bucket())
                .key(key)
                .build());
    }
    public boolean exists(String key) {
        try {
            s3Client.headObject(HeadObjectRequest.builder()
                    .bucket(s3Properties.bucket())
                    .key(key)
                    .build());
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        }
    }


    private boolean isNotPermitted(String example){
        for(var ext: PermittedExtensions.values()){
            if(ext.toString().compareToIgnoreCase(example)==0)
                return false ;
        }
        return  true;
    }
}
