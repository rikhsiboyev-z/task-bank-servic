package com.example.banksystem.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith("Bearer")) {

            filterChain.doFilter(request, response);
            return;
        }

        String substring = header.substring(7);
        Claims claims = jwtService.claims(substring);
        String subject = claims.getSubject();

        UserDetails userDetails = userDetailsService.loadUserByUsername(subject);

        UsernamePasswordAuthenticationToken authenticated =
                UsernamePasswordAuthenticationToken
                        .authenticated(null, userDetails, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticated);

        filterChain.doFilter(request, response);

    }
}
