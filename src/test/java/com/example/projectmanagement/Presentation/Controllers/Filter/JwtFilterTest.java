package com.example.projectmanagement.Presentation.Controllers.Filter;

import com.example.projectmanagement.Application.Services.JwtClaims;
import com.example.projectmanagement.Application.Services.JwtService;
import com.example.projectmanagement.Presentation.Errors.ErrorWriter;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class JwtFilterTest {
    private final JwtService jwtService = mock(JwtService.class);
    private final ErrorWriter errorWriter = mock(ErrorWriter.class);
    private final JwtFilter filter = new JwtFilter(jwtService, errorWriter);
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final FilterChain chain = mock(FilterChain.class);

    @AfterEach
    void clearMdc() {
        MDC.clear();
    }

    @Test
    void authenticatedRequestExposesUserIdToDownstreamAndClearsItAfterward() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer valid-token");
        when(jwtService.parse("valid-token")).thenReturn(new JwtClaims(42L, "user@example.com", Set.of()));
        org.mockito.Mockito.doAnswer(invocation -> {
            assertEquals("42", MDC.get("userId"));
            return null;
        }).when(chain).doFilter(request, response);

        filter.doFilterInternal(request, response, chain);

        assertNull(MDC.get("userId"));
        verifyNoInteractions(errorWriter);
    }

    @Test
    void userIdIsClearedWhenDownstreamThrows() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer valid-token");
        when(jwtService.parse("valid-token")).thenReturn(new JwtClaims(42L, "user@example.com", Set.of()));
        org.mockito.Mockito.doThrow(new jakarta.servlet.ServletException("downstream failure"))
                .when(chain).doFilter(request, response);

        assertThrows(jakarta.servlet.ServletException.class,
                () -> filter.doFilterInternal(request, response, chain));

        assertNull(MDC.get("userId"));
    }

    @Test
    void missingAuthorizationDoesNotSetUserId() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilterInternal(request, response, chain);

        assertNull(MDC.get("userId"));
        verify(chain).doFilter(request, response);
        verifyNoInteractions(jwtService, errorWriter);
    }

    @Test
    void invalidTokenDoesNotSetUserId() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid-token");
        when(jwtService.parse("invalid-token")).thenThrow(new JwtException("invalid"));

        filter.doFilterInternal(request, response, chain);

        assertNull(MDC.get("userId"));
        verifyNoInteractions(chain);
        verify(errorWriter).write(request, response, 401, "INVALID_TOKEN",
                "Invalid or expired bearer token");
    }
}
