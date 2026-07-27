package com.example.projectmanagement.Application.Dtos;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
public record UserLoginDtos(@NotBlank @Email String email,@NotBlank String password) {

}
