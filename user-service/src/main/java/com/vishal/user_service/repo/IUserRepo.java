package com.vishal.user_service.repo;

import com.vishal.user_service.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserRepo extends JpaRepository<Users, UUID> {

    Optional<Users> findByEmail(String email);
    Optional<Users> findByUsername(String username);
    boolean existsByUsernameOrEmail(String username, String email);

    @Modifying
    @Query("UPDATE Users u SET u.totalFollowers = u.totalFollowers + 1 WHERE u.id = :userId")
    void incrementFollowerCount(@Param("userId") UUID userId);

    @Modifying
    @Query("UPDATE Users u SET u.totalFollowers = u.totalFollowers - 1 WHERE u.id = :userId")
    void decrementFollowerCount(@Param("userId") UUID userId);

    @Modifying
    @Query("UPDATE Users u SET u.totalFollowers = u.totalFollowers + :increment WHERE u.id = :userId")
    void updateFollowerCount(@Param("userId") UUID userId, @Param("increment") long increment);
}
