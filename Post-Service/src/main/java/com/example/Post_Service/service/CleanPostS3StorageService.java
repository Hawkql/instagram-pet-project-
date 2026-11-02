package com.example.Post_Service.service;

import com.example.Post_Service.config.S3Properties;
import com.example.Post_Service.repository.PostRepository;
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
public class CleanPostS3StorageService {
    private final S3Client s3Client;
    private final PostRepository postRepository;
    private final Logger logger = LoggerFactory.getLogger(CleanPostS3StorageService.class);
    private final  S3Properties props;
    public CleanPostS3StorageService(S3Client s3Client, PostRepository postRepository, S3Properties props) {
        this.s3Client = s3Client;
        this.postRepository = postRepository;
        this.props = props;
    }

    public void cleaned() {
        ListObjectsV2Request request = ListObjectsV2Request.builder().
                bucket(props.bucket())
                .prefix("post/")
                .build();


        Instant thirtyDaysAgo = Instant.now().minus(30, ChronoUnit.DAYS);
        Set<String> usedKey = postRepository.findAllMediaUrlPost(thirtyDaysAgo);

        Instant oneDayEgo = Instant.now().minus(1, ChronoUnit.DAYS);
        List<ObjectIdentifier> listDelete = new ArrayList<>();
        for (ListObjectsV2Response page : s3Client.listObjectsV2Paginator(request)) {
            for (S3Object object : page.contents()) {
                if (object.lastModified().isAfter(oneDayEgo))
                    continue;
                if (usedKey.contains(object.key()))
                    continue;
                listDelete.add(ObjectIdentifier.builder().key(object.key()).build());

            }
        }

        if (listDelete.isEmpty()) {
            logger.info("Нечего удалять");
            return;
        }

        for (int i = 0; i < listDelete.size(); i += 1000) {
            List<ObjectIdentifier> batch = listDelete.subList(i, Math.min(i + 1000, listDelete.size()));
            DeleteObjectsResponse resp = s3Client.deleteObjects(DeleteObjectsRequest.builder()
                    .bucket(props.bucket())
                    .delete(Delete.builder().objects(batch).build())
                    .build());
            logger.info("Удалено {}, ошибок {}", resp.deleted().size(), resp.errors().size());
        }
        postRepository.deleteSoftDeletedBefore(thirtyDaysAgo);
    }
}
