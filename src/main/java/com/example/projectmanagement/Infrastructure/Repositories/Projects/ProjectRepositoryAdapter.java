package com.example.projectmanagement.Infrastructure.Repositories.Projects;

import com.example.projectmanagement.Domain.Entities.Project;
import com.example.projectmanagement.Domain.Entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class ProjectRepositoryAdapter implements ProjectAdapter {

    private final ProjectRepository repository;

    @Override
    public Optional<Project> findById(long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Project> findByIdAndOwnerId(long id, long ownerId) {
        return repository.findByIdAndUserID_Id(id, ownerId);
    }

    @Override
    public Project save(Project project) {
        return repository.save(project);
    }
}
