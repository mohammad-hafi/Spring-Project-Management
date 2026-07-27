package com.example.projectmanagement.Application.Services;

import java.util.Set;

public record JwtClaims(Long userId, String email, Set<String> permissions) {}
