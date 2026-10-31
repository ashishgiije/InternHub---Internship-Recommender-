package com.example.recommender.service.impl;

import com.example.recommender.dto.AuthResponse;
import com.example.recommender.dto.LoginRequest;
import com.example.recommender.dto.RegisterRequest;
import com.example.recommender.dto.UserProfileUpdateRequest;
import com.example.recommender.entity.User;
import com.example.recommender.exception.ResourceNotFoundException;
import com.example.recommender.exception.UnauthorizedException;
import com.example.recommender.repository.UserRepository;
import com.example.recommender.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new UnauthorizedException("Email is already registered.");
        }

        User user = new User();
        user.setName(request.getName().trim());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword().trim()));
        User saved = userRepository.save(user);
        return mapToAuthResponse(saved);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password."));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password.");
        }
        return mapToAuthResponse(user);
    }

    @Override
    public AuthResponse updateProfile(Integer userId, UserProfileUpdateRequest request) {
        User user = getUserById(userId);
        user.setSkills(request.getSkills().trim());
        user.setInterests(request.getInterests().trim());
        user.setActivities(request.getActivities().trim());
        return mapToAuthResponse(userRepository.save(user));
    }

    @Override
    public AuthResponse getUser(Integer userId) {
        return mapToAuthResponse(getUserById(userId));
    }

    private User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    private AuthResponse mapToAuthResponse(User user) {
        AuthResponse response = new AuthResponse();
        response.setUserId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setSkills(user.getSkills());
        response.setInterests(user.getInterests());
        response.setActivities(user.getActivities());
        return response;
    }
}
