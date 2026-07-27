package com.example.projectmanagement.Infrastructure.Repositories.Projects;

import com.example.projectmanagement.Domain.Entities.Project;
import com.example.projectmanagement.Domain.Entities.User;

import java.util.Optional;

public interface ProjectAdapter {
    Optional<Project> findById(long id);
    Optional<Project> findByIdAndOwnerId(long id, long ownerId);
    Project save(Project project);
}
