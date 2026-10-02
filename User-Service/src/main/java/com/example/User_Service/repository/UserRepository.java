package com.example.User_Service.repository;

import com.example.User_Service.domain.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    boolean existsByEmail(String email);
    boolean existsByUserName(String userName);

    UserEntity findByUserName(String userName);

    @Query("""
    SELECT u.id FROM UserEntity u
    Where (:name IS NULL OR u.name =:name)
    AND (:family IS NULL OR u.family = :family)
    AND(:userName IS NULL OR u.userName= :userName)
    """)
    List<UserEntity> searchAllByFilter(@Param("name") String name,
                                       @Param("family") String family,
                                       @Param("userName") String userName,
                                       Pageable pageable);


}
