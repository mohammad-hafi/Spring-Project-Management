package com.example.projectmanagement.Infrastructure.Repositories.Projects;

import com.example.projectmanagement.Domain.Entities.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<Project> findByOwnerId(long ownerId, Pageable pageable) {
        return repository.findByUserID_Id(ownerId, pageable);
    }

    @Override
    public Project save(Project project) {
        return repository.save(project);
    }
}
