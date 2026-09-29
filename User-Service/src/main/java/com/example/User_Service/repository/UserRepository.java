package com.example.User_Service.repository;

import com.example.User_Service.domain.UserEntity;
import org.apache.kafka.common.Uuid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public class UserRepository extends JpaRepository<UserEntity, UUID> {
    boolean existsByEmail(String email);
}
