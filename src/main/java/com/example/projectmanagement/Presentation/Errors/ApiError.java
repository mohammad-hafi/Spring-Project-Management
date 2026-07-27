package com.example.projectmanagement.Presentation.Errors;

import java.time.Instant;

public record ApiError(
        int status,
        String code,
        String message,
        String path,
        Instant timestamp,
        String correlationId
) {}
