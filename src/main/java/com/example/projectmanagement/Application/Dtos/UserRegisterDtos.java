package com.example.projectmanagement.Application.Dtos;

import com.example.projectmanagement.Domain.Entities.Status;
import jakarta.validation.constraints.*;


public record UserRegisterDtos(

        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 50,
                message = "Name must be between 2 and 50 characters")
        @Pattern(
                regexp = "^[a-zA-Z ]+$",
                message = "Name can only contain letters and spaces"
        )
        String name,

        @NotBlank(message = "Description is required")
        @Size(max = 500,
                message = "Description cannot exceed 500 characters")
        String description,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,
        @NotBlank @Size(max = 50) String department,
        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 100,
                message = "Password must be between 8 and 100 characters")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).*$",
                message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"
        )
        String password,

        @NotBlank @Size(max = 50) String jobTitle,
        @NotBlank @Size(max = 50) String statusId
) {

}
