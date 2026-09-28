package com.example.projectmanagement.Application.UseCase;

import com.example.projectmanagement.Application.Dtos.*;
import com.example.projectmanagement.Application.Interfaces.UserCases;
import com.example.projectmanagement.Application.Services.JwtService;
import com.example.projectmanagement.Domain.*;
import com.example.projectmanagement.Domain.Entities.Status;
import com.example.projectmanagement.Domain.Entities.User;
import com.example.projectmanagement.Infrastructure.Repositories.AuthorizationRepository;
import com.example.projectmanagement.Infrastructure.Repositories.StatusRepository;
import com.example.projectmanagement.Infrastructure.Repositories.Users.UserAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Locale;
import java.util.Set;

@Service @RequiredArgsConstructor
public class UserUseCase implements UserCases {
    private final UserAdapter users;
    private final StatusRepository statuses;
    private final AuthorizationRepository authorization;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    @Override @Transactional
    public UserResponseDto registerUser(UserRegisterDtos request) {
        String email = normalize(request.email());
        if (users.existsByEmail(email)) throw new ConflictException("Email already exists");
        Status status = statuses.findByNameIgnoreCase(request.statusId().trim())
                .orElseThrow(() -> new NotFoundException("Status not found"));
        User user = new User();
        user.setName(request.name().trim()); user.setDescription(request.description().trim());
        user.setDepartment(request.department().trim()); user.setJobTitle(request.jobTitle().trim());
        user.setCreatedOn(Instant.now()); user.setEmail(email); user.setStatusID(status);
        user.setPassword(encoder.encode(request.password()));
        try {
            user = users.save(user);
            if (authorization.assignMemberRole(user.getId()) != 1) throw new IllegalStateException("MEMBER role is not configured");
            return response(user);
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException("Email already exists");
        }
    }

    @Override @Transactional(readOnly = true)
    public LoginResponseDto loginUser(UserLoginDtos dto) {
        User user = users.findByEmail(normalize(dto.email())).orElseThrow(InvalidCredentialsException::new);
        if (!encoder.matches(dto.password(), user.getPassword())) throw new InvalidCredentialsException();
        Set<String> permissions = authorization.findPermissionTags(user.getId());
        return new LoginResponseDto(jwt.generateToken(user.getEmail(), user.getId(), permissions));
    }

    private static String normalize(String email) { return email.trim().toLowerCase(Locale.ROOT); }
    private static UserResponseDto response(User u) {
        return new UserResponseDto(u.getId(),u.getName(),u.getDescription(),u.getEmail(),u.getDepartment(),
                u.getJobTitle(),u.getCreatedOn(),u.getStatusID().getId());
    }
}
