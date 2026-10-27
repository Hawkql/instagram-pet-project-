package com.example.Post_Service.repository;

import com.example.Post_Service.domain.LikePostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface LikePostRepository extends JpaRepository<LikePostEntity, UUID> {

    LikePostEntity existsByIdAndIdPost(UUID id, UUID idPost);

}
