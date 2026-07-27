package com.example.projectmanagement.Infrastructure.Repositories.Projects;

import com.example.projectmanagement.Domain.Entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByIdAndUserID_Id(long id, long ownerId);
}
