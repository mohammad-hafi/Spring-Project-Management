package com.example.projectmanagement.Application.Interfaces;
import com.example.projectmanagement.Application.Dtos.*;
public interface ProjectCases {
    ProjectResponseDto addProject(ProjectCreateDto dto,long userId);
    ProjectResponseDto updateProject(long projectId,ProjectUpdateDto dto,long userId);
    ProjectResponseDto getProjectById(long id,long userId);
}
