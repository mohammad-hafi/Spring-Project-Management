package com.example.projectmanagement.Infrastructure.Repositories.Users;

import com.example.projectmanagement.Domain.Entities.User;

import java.util.Optional;


public interface UserAdapter {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findById(long id);
    User save(User user);
}
