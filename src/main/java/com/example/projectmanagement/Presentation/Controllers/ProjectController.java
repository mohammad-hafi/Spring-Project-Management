package com.example.projectmanagement.Presentation.Controllers;
import com.example.projectmanagement.Application.Dtos.*;
import com.example.projectmanagement.Application.Interfaces.ProjectCases;
import com.example.projectmanagement.Application.Services.UserPrincipal;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestController @RequestMapping("/projects")
@RequiredArgsConstructor
@SecurityRequirement(name="bearerAuth")
public class ProjectController {

 private final ProjectCases projects;

 @PostMapping
 @PreAuthorize("hasAuthority('project:create')")
 public ResponseEntity<ProjectResponseDto> create(@Valid @RequestBody ProjectCreateDto dto,
   @AuthenticationPrincipal UserPrincipal principal) {
  ProjectResponseDto created=projects.addProject(dto,principal.id());
  return ResponseEntity.created(URI.create("/projects/"+created.id())).body(created);
 }
 @PutMapping("/{id}") @PreAuthorize("hasAuthority('project:update')")
 public ResponseEntity<ProjectResponseDto> update(@PathVariable long id,@Valid @RequestBody ProjectUpdateDto dto,
   @AuthenticationPrincipal UserPrincipal principal) {
  return ResponseEntity.ok(projects.updateProject(id,dto,principal.id()));
 }
 @GetMapping("/{id}") @PreAuthorize("hasAuthority('project:read')")
 public ResponseEntity<ProjectResponseDto> get(@PathVariable long id,@AuthenticationPrincipal UserPrincipal principal) {
  return ResponseEntity.ok(projects.getProjectById(id,principal.id()));
 }
}
