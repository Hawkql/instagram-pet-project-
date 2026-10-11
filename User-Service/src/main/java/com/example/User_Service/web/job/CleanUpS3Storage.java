package com.example.User_Service.web.job;

import com.example.User_Service.config.S3Properties;
import com.example.User_Service.service.CleanS3StorageService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CleanUpS3Storage {
    private final CleanS3StorageService service;
    private final  S3Properties props;
    public CleanUpS3Storage(CleanS3StorageService service,S3Properties props) {
        this.service = service;
        this.props=props;
    }
    @Scheduled(cron = "0 0 3 * * *")
    public  void cleanStorage(){
        service.clened(props);
    }
}
