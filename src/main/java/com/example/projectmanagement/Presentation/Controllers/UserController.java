package com.example.projectmanagement.Presentation.Controllers;
import com.example.projectmanagement.Application.Dtos.*;
import com.example.projectmanagement.Application.Interfaces.UserCases;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
@RestController
@RequestMapping("/Users")
@RequiredArgsConstructor
@Tag(name = "Access", description = "Create an account and obtain an access token.")
public class UserController {
 private final UserCases service;
 @PostMapping("/register")
 @Operation(summary = "Create an account", description = "Registers a member account with the default role.")
 public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserRegisterDtos dto) {
  UserResponseDto created=service.registerUser(dto);
  return ResponseEntity.created(URI.create("/Users/"+created.id())).body(created);
 }
 @PostMapping("/login")
 @Operation(summary = "Log in", description = "Returns a JWT access token for authenticated project requests.")
 public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody UserLoginDtos dto) {
  return ResponseEntity.ok(service.loginUser(dto));
 }
}
