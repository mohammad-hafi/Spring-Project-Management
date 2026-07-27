package com.example.projectmanagement.Application.Dtos;
import java.time.Instant;
public record UserResponseDto(Long id,String name,String description,String email,String department,
 String jobTitle,Instant createdOn,Integer statusId) {}
