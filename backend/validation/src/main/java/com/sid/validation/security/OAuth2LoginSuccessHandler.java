package com.sid.validation.security;

import com.sid.validation.entity.User;
import com.sid.validation.repository.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public OAuth2LoginSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(
            jakarta.servlet.http.HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");
        String html_url = oauthUser.getAttribute("html_url");
        
        if(email==null) {
        	email=html_url;
        }

        String authProvider = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();

        // Check if the user exists, if not create one
        Optional<User> existingUser = userRepository.findByUsername(email);

        if (existingUser.isEmpty()) {
            User newUser = new User();
            newUser.setUsername(email);
            newUser.setAuthProvider(authProvider.toUpperCase()); // Set provider dynamically
            newUser.setPassword(null); // No password for OAuth2 users
            userRepository.save(newUser);
        }

        // Generate JWT
        String token = JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpiration))
                .sign(Algorithm.HMAC512(jwtSecret));

        // Store JWT in HttpOnly cookie
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // Use true if using HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60); // 1 hour expiration

        response.addCookie(cookie);

        // Redirect the popup to a success page or close it
        String script = "<script>window.close();</script>";
        response.setContentType("text/html");
        response.getWriter().write(script);
    }
}
