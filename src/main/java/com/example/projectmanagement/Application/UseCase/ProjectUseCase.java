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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectUseCase implements ProjectCases {
    private final ProjectAdapter projects;
    private final UserAdapter users;
    private final StatusRepository statuses;

    @Override
    @Transactional(readOnly = true)
    public PaginatedProjectsResponseDto getProjects(long userId, int pageNumber, int pageSize) {
        Page<Project> page = projects.findByOwnerId(
                userId,
                PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Direction.DESC, "createdOn"))
        );
        List<ProjectResponseDto> items = page.getContent().stream().map(ProjectUseCase::response).toList();
        return new PaginatedProjectsResponseDto(
                items,
                page.getTotalElements(),
                page.getTotalPages(),
                pageNumber,
                pageSize
        );
    }

    @Override @Transactional
    public ProjectResponseDto addProject(ProjectCreateDto dto,long userId) {
        try {
            User owner = users.findById(userId).orElseThrow(() -> {
                log.atWarn().log("Project owner not found");
                return new NotFoundException("User not found");
            });
            Status status = status(dto.statusId());
            Instant now = Instant.now();
            Project project = new Project();
            project.setName(dto.name().trim()); project.setDescription(dto.description()); project.setUserID(owner);
            project.setStatusID(status); project.setPriorityLevel(dto.priorityLevel()); project.setTargetDate(dto.targetDate());
            project.setStartDate(now); project.setCreatedOn(now);
            Project saved = projects.save(project);
            log.atInfo()
                    .addKeyValue("projectId", saved.getId())
                    .log("Project created");
            return response(saved);
        } catch (NotFoundException exception) {
            throw exception;
        } catch (RuntimeException exception) {
            log.atError()
                    .setCause(exception)
                    .log("Failed to create project");
            throw exception;
        }
    }

    @Override @Transactional
    public ProjectResponseDto updateProject(long projectId,ProjectUpdateDto dto,long userId) {
        try {
            Project project = owned(projectId,userId);
            project.setName(dto.name().trim()); project.setDescription(dto.description());
            project.setStatusID(status(dto.statusId())); project.setPriorityLevel(dto.priorityLevel());
            project.setTargetDate(dto.targetDate()); project.setModifiedOn(Instant.now());
            Project saved = projects.save(project);
            log.atInfo()
                    .addKeyValue("projectId", saved.getId())
                    .log("Project updated");
            return response(saved);
        } catch (NotFoundException exception) {
            throw exception;
        } catch (RuntimeException exception) {
            log.atError()
                    .addKeyValue("projectId", projectId)
                    .setCause(exception)
                    .log("Failed to update project");
            throw exception;
        }
    }

    @Override @Transactional(readOnly=true)
    public ProjectResponseDto getProjectById(long id,long userId) {
        try {
            ProjectResponseDto project = response(owned(id,userId));
            log.atInfo()
                    .addKeyValue("projectId", id)
                    .log("Project retrieved");
            return project;
        } catch (NotFoundException exception) {
            throw exception;
        } catch (RuntimeException exception) {
            log.atError()
                    .addKeyValue("projectId", id)
                    .setCause(exception)
                    .log("Failed to retrieve project");
            throw exception;
        }
    }

    @Override
    @Transactional
    public void deleteProject(long projectId,long userId) {
        try {
            Project project = owned(projectId,userId);
            projects.delete(project);
            log.atInfo()
                    .addKeyValue("projectId", projectId)
                    .log("Project deleted");
        } catch (NotFoundException exception) {
            throw exception;
        } catch (RuntimeException exception) {
            log.atError()
                    .addKeyValue("projectId", projectId)
                    .setCause(exception)
                    .log("Failed to delete project");
            throw exception;
        }
    }

    private Project owned(long id,long userId) {
        Project project = projects.findById(id).orElseThrow(() -> {
            log.atWarn()
                    .addKeyValue("projectId", id)
                    .log("Project not found");
            return new NotFoundException("Project not found");
        });
        if (!project.getUserID().getId().equals(userId)) {
            log.atWarn()
                    .addKeyValue("projectId", id)
                    .log("Unauthorized project access");
            throw new NotFoundException("Project not found");
        }
        return project;
    }

    private Status status(int id) {
        return statuses.findById(id).orElseThrow(() -> {
            log.atWarn()
                    .addKeyValue("statusId", id)
                    .log("Project status not found");
            return new NotFoundException("Status not found");
        });
    }

    private static ProjectResponseDto response(Project p) {
        return new ProjectResponseDto(p.getId(),p.getName(),p.getDescription(),p.getCreatedOn(),p.getModifiedOn(),
                p.getStartDate(),p.getTargetDate(),p.getPriorityLevel(),p.getStatusID().getId(),p.getUserID().getId());
    }
}
