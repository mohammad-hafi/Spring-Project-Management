package com.example.projectmanagement.Application.Dtos;

import java.util.List;

public record PaginatedProjectsResponseDto(
        List<ProjectResponseDto> items,
        long totalCount,
        int totalPages,
        int pageNumber,
        int pageSize
) {}
