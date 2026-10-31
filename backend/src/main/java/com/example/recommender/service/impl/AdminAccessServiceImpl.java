package com.example.recommender.service.impl;

import com.example.recommender.exception.UnauthorizedException;
import com.example.recommender.service.AdminAccessService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AdminAccessServiceImpl implements AdminAccessService {

    @Value("${app.admin.passcode}")
    private String configuredAdminPass;

    @Override
    public void validateAdminPass(String adminPass) {
        if (adminPass == null || !configuredAdminPass.equals(adminPass.trim())) {
            throw new UnauthorizedException("Invalid admin passcode.");
        }
    }
}
