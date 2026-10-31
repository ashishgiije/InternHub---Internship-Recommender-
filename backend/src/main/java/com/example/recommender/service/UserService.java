package com.example.recommender.service;

import com.example.recommender.dto.AuthResponse;
import com.example.recommender.dto.LoginRequest;
import com.example.recommender.dto.RegisterRequest;
import com.example.recommender.dto.UserProfileUpdateRequest;

public interface UserService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse updateProfile(Integer userId, UserProfileUpdateRequest request);

    AuthResponse getUser(Integer userId);
}
