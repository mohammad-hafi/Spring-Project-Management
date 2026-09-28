package com.example.projectmanagement.Presentation.Controllers.Filter;

import com.example.projectmanagement.Application.Services.JwtClaims;
import com.example.projectmanagement.Application.Services.JwtService;
import com.example.projectmanagement.Application.Services.UserPrincipal;
import com.example.projectmanagement.Presentation.Errors.ErrorWriter;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final ErrorWriter errorWriter;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI().substring(request.getContextPath().length());
        return "/Users/login".equals(path) || "/Users/register".equals(path);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header == null) {
            chain.doFilter(request, response);
            return;
        }
        if (!header.startsWith("Bearer ") || header.length() == 7) {
            errorWriter.write(request, response, HttpStatus.UNAUTHORIZED.value(),
                    "INVALID_TOKEN", "Invalid bearer token");
            return;
        }
        try {
            JwtClaims claims = jwtService.parse(header.substring(7));
            UserPrincipal principal = new UserPrincipal(claims.userId(), claims.email(), claims.permissions());
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    principal, null, principal.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            MDC.put("userId", String.valueOf(claims.userId()));
            try {
                chain.doFilter(request, response);
            } finally {
                MDC.remove("userId");
            }
        } catch (JwtException | IllegalArgumentException ex) {
            SecurityContextHolder.clearContext();
            errorWriter.write(request, response, HttpStatus.UNAUTHORIZED.value(),
                    "INVALID_TOKEN", "Invalid or expired bearer token");
        }
    }
}
