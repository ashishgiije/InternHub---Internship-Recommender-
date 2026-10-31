package com.example.recommender.controller;

import com.example.recommender.dto.AuthResponse;
import com.example.recommender.dto.InternshipApplicationDto;
import com.example.recommender.dto.ResumeExtractResponse;
import com.example.recommender.dto.UserProfileUpdateRequest;
import com.example.recommender.service.ResumeService;
import com.example.recommender.service.ApplicationService;
import com.example.recommender.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final ApplicationService applicationService;
    private final ResumeService resumeService;

    public UserController(UserService userService,
                          ApplicationService applicationService,
                          ResumeService resumeService) {
        this.userService = userService;
        this.applicationService = applicationService;
        this.resumeService = resumeService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<AuthResponse> getUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @PutMapping("/{userId}/profile")
    public ResponseEntity<AuthResponse> updateProfile(@PathVariable Integer userId,
                                                      @Valid @RequestBody UserProfileUpdateRequest request) {
        return ResponseEntity.ok(userService.updateProfile(userId, request));
    }

    @PostMapping("/{userId}/applications/{internshipId}")
    public ResponseEntity<InternshipApplicationDto> applyInternship(@PathVariable Integer userId,
                                                                    @PathVariable Integer internshipId) {
        return ResponseEntity.ok(applicationService.applyForInternship(userId, internshipId));
    }

    @GetMapping("/{userId}/applications")
    public ResponseEntity<List<InternshipApplicationDto>> getUserApplications(@PathVariable Integer userId) {
        return ResponseEntity.ok(applicationService.getUserApplications(userId));
    }

    @PostMapping("/{userId}/resume-extract")
    public ResponseEntity<ResumeExtractResponse> extractResume(@PathVariable Integer userId,
                                                               @RequestParam("file") MultipartFile file) {
        userService.getUser(userId);
        return ResponseEntity.ok(resumeService.extractResumeData(file));
    }
}
