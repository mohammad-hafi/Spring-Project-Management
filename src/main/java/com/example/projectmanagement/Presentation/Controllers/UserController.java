package com.example.projectmanagement.Presentation.Controllers;
import com.example.projectmanagement.Application.Dtos.*;
import com.example.projectmanagement.Application.Interfaces.UserCases;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
@RestController @RequestMapping("/user") @RequiredArgsConstructor
public class UserController {
 private final UserCases service;
 @PostMapping("/register")
 public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserRegisterDtos dto) {
  UserResponseDto created=service.registerUser(dto);
  return ResponseEntity.created(URI.create("/user/"+created.id())).body(created);
 }
 @PostMapping("/login")
 public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody UserLoginDtos dto) {
  return ResponseEntity.ok(service.loginUser(dto));
 }
}
