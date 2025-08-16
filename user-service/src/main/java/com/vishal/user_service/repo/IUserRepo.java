package com.vishal.user_service.repo;

import com.vishal.user_service.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserRepo extends JpaRepository<Users, UUID> {

    Optional<Users> findByEmail(String email);
}
