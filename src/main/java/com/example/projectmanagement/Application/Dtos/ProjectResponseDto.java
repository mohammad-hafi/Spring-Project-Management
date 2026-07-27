package com.example.projectmanagement.Application.Dtos;
import java.time.Instant;
public record ProjectResponseDto(Long id,String name,String description,Instant createdOn,
 Instant modifiedOn,Instant startDate,Instant targetDate,Integer priorityLevel,Integer statusId,Long ownerId) {}
