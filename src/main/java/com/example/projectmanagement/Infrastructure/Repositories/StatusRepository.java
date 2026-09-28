package com.example.projectmanagement.Infrastructure.Repositories;
import com.example.projectmanagement.Domain.Entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status,Integer> {
    Optional<Status> findByNameIgnoreCase(String name);
}
