package com.sid.validation.service;

import com.sid.validation.dto.UserDTO;
import com.sid.validation.entity.User;
import com.sid.validation.repository.UserRepository;
import com.sid.validation.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public void registerUser(UserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new RuntimeException("Username is already taken.");
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        userRepository.save(user);
    }

    public String authenticateUser(UserDTO userDTO) {
        User user = userRepository.findByUsername(userDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found."));

        if (passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            return jwtUtil.generateToken(user.getUsername()); // Return JWT token
        } else {
            throw new RuntimeException("Invalid username or password.");
        }
    }

	public boolean validateToken(String token) {
		return jwtUtil.validateTokenAndRetrieveSubject(token)!=null;
	}
}