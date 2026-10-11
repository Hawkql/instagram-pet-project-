package com.example.User_Service.service;

import com.example.User_Service.config.S3Properties;
import com.example.User_Service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CleanS3StorageService {
   private final S3Client s3Client;
   private final UserRepository userRepository;
   private final Logger logger = LoggerFactory.getLogger(CleanS3StorageService.class);

    public CleanS3StorageService(S3Client s3Client, UserRepository userRepository) {
        this.s3Client = s3Client;
        this.userRepository = userRepository;
    }

    public void clened(S3Properties props) {
        ListObjectsV2Request request = ListObjectsV2Request.builder().
                bucket(props.bucket())
                .prefix("avatars/")
                .build();


        Set<String> listUser=  userRepository.findAllAvatarKeys();


        Instant oneDayEgo = Instant.now().minus(1,ChronoUnit.DAYS);
        List<ObjectIdentifier> listDelete = new ArrayList<>();
        for (ListObjectsV2Response page : s3Client.listObjectsV2Paginator(request)){
            for (S3Object object:page.contents()){
                if(object.lastModified().isAfter(oneDayEgo))
                    continue;
                if(listUser.contains(object.key()))
                    continue;
                listDelete.add(ObjectIdentifier.builder().key(object.key()).build());
            }
        }

        if(listDelete.isEmpty()) {
            logger.info("Нечего удалять");
            return;
        }
        DeleteObjectsRequest deleteRequest = DeleteObjectsRequest.builder()
                .bucket(props.bucket())
                .delete(Delete.builder().objects(listDelete).build())
                .build();


        DeleteObjectsResponse deleteResponse = s3Client.deleteObjects(deleteRequest);
        logger.info("Удалено {} файлов", listDelete.size());
        logger.info("Ошибки удаления произшли в {} файлов",deleteResponse.errors().size());
        logger.info("Список не удалившихся файлов{}",deleteResponse.errors().stream().toList());

    }
}
