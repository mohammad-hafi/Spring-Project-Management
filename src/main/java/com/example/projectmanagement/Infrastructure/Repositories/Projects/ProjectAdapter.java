package com.example.projectmanagement.Infrastructure.Repositories.Projects;

import com.example.projectmanagement.Domain.Entities.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProjectAdapter {
    Optional<Project> findById(long id);
    Optional<Project> findByIdAndOwnerId(long id, long ownerId);
    Page<Project> findByOwnerId(long ownerId, Pageable pageable);
    Project save(Project project);
}
