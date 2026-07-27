package com.example.projectmanagement.Application.Interfaces;
import com.example.projectmanagement.Application.Dtos.*;
public interface UserCases {
    UserResponseDto registerUser(UserRegisterDtos user);
    LoginResponseDto loginUser(UserLoginDtos dto);
}
