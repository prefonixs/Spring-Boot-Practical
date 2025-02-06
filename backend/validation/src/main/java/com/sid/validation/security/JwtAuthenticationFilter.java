package com.sid.validation.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Skip filtering for permitted endpoints
        String requestURI = request.getRequestURI();
        if (requestURI.equals("/register") || requestURI.equals("/") || requestURI.equals("/logout")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Check if cookies are present
        Cookie[] cookies = request.getCookies();
        Optional<String> jwtToken = Optional.empty();

        if (cookies != null) {
            jwtToken = Arrays.stream(cookies)
                    .filter(cookie -> "jwt".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst();
        }

        if (jwtToken.isPresent() && SecurityContextHolder.getContext().getAuthentication() == null) {
            String username = jwtUtil.validateTokenAndRetrieveSubject(jwtToken.get());

            if (username != null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
