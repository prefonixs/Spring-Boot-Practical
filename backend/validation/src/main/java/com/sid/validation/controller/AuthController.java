package com.sid.validation.controller;

import com.sid.validation.dto.UserDTO;
import com.sid.validation.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {
		userService.registerUser(userDTO);
		return ResponseEntity.ok("User registered successfully.");
	}

	@PostMapping("/login")
	public ResponseEntity<String> loginUser(@RequestBody UserDTO userDTO, HttpServletResponse response) {
		String token = userService.authenticateUser(userDTO);

		// Store JWT in HttpOnly cookie
		Cookie cookie = new Cookie("jwt", token);
		cookie.setHttpOnly(true); // Prevent JavaScript access
		cookie.setSecure(true); // Use only with HTTPS
		cookie.setPath("/"); // Available for all endpoints
		cookie.setMaxAge(60 * 60); // 1 hour expiration

		response.addCookie(cookie);
		
		return ResponseEntity.ok("Login successful");
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout(HttpServletResponse response) {
		Cookie cookie = new Cookie("jwt", "");
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setPath("/");
		cookie.setMaxAge(0); // Expire immediately

		response.addCookie(cookie);

		return ResponseEntity.ok("Logout successful");
	}
	
	@GetMapping("/auth")
    public ResponseEntity<String> checkAuth(HttpServletRequest request) {
        // Extract the JWT cookie from the request
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    // Validate the token (you can use your UserService or a JWT utility class)
                    if (userService.validateToken(token)) {
                        return ResponseEntity.ok("Authenticated");
                    }
                }
            }
        }
        // If no valid JWT cookie is found, return 401 Unauthorized
        return ResponseEntity.status(401).body("Unauthorized");
    }
}
