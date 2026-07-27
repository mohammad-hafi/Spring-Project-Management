package com.example.projectmanagement.Application.Dtos;
import jakarta.validation.constraints.*;
import java.time.Instant;
public record ProjectUpdateDto(@NotBlank @Size(min=3,max=50) String name,
 @Size(max=100) String description, @NotNull @Future Instant targetDate,
 @NotNull @Min(1) @Max(5) Integer priorityLevel, @NotNull @Positive Integer statusId) {}
