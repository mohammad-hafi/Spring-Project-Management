package com.example.projectmanagement.Application.UseCase;

import com.example.projectmanagement.Application.Dtos.*;
import com.example.projectmanagement.Application.Interfaces.ProjectCases;
import com.example.projectmanagement.Domain.NotFoundException;
import com.example.projectmanagement.Domain.Entities.Project;
import com.example.projectmanagement.Domain.Entities.Status;
import com.example.projectmanagement.Domain.Entities.User;
import com.example.projectmanagement.Infrastructure.Repositories.Projects.ProjectAdapter;
import com.example.projectmanagement.Infrastructure.Repositories.StatusRepository;
import com.example.projectmanagement.Infrastructure.Repositories.Users.UserAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service @RequiredArgsConstructor
public class ProjectUseCase implements ProjectCases {
    private final ProjectAdapter projects;
    private final UserAdapter users;
    private final StatusRepository statuses;

    @Override @Transactional
    public ProjectResponseDto addProject(ProjectCreateDto dto,long userId) {
        User owner = users.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Status status = status(dto.statusId());
        Instant now = Instant.now();
        Project project = new Project();
        project.setName(dto.name().trim()); project.setDescription(dto.description()); project.setUserID(owner);
        project.setStatusID(status); project.setPriorityLevel(dto.priorityLevel()); project.setTargetDate(dto.targetDate());
        project.setStartDate(now); project.setCreatedOn(now);
        return response(projects.save(project));
    }

    @Override @Transactional
    public ProjectResponseDto updateProject(long projectId,ProjectUpdateDto dto,long userId) {
        Project project = owned(projectId,userId);
        project.setName(dto.name().trim()); project.setDescription(dto.description());
        project.setStatusID(status(dto.statusId())); project.setPriorityLevel(dto.priorityLevel());
        project.setTargetDate(dto.targetDate()); project.setModifiedOn(Instant.now());
        return response(projects.save(project));
    }

    @Override @Transactional(readOnly=true)
    public ProjectResponseDto getProjectById(long id,long userId) { return response(owned(id,userId)); }

    private Project owned(long id,long userId) {
        return projects.findByIdAndOwnerId(id,userId).orElseThrow(() -> new NotFoundException("Project not found"));
    }
    private Status status(int id) { return statuses.findById(id).orElseThrow(() -> new NotFoundException("Status not found")); }
    private static ProjectResponseDto response(Project p) {
        return new ProjectResponseDto(p.getId(),p.getName(),p.getDescription(),p.getCreatedOn(),p.getModifiedOn(),
                p.getStartDate(),p.getTargetDate(),p.getPriorityLevel(),p.getStatusID().getId(),p.getUserID().getId());
    }
}