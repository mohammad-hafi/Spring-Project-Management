package com.example.projectmanagement.Infrastructure.Repositories.Users;

import com.example.projectmanagement.Domain.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
