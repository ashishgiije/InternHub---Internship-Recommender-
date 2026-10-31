package com.example.recommender.service.impl;

import com.example.recommender.dto.InternshipApplicationDto;
import com.example.recommender.entity.Internship;
import com.example.recommender.entity.InternshipApplication;
import com.example.recommender.entity.User;
import com.example.recommender.exception.ResourceNotFoundException;
import com.example.recommender.exception.UnauthorizedException;
import com.example.recommender.repository.InternshipApplicationRepository;
import com.example.recommender.repository.InternshipRepository;
import com.example.recommender.repository.UserRepository;
import com.example.recommender.service.AdminAccessService;
import com.example.recommender.service.ApplicationService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final InternshipApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final InternshipRepository internshipRepository;
    private final AdminAccessService adminAccessService;

    public ApplicationServiceImpl(InternshipApplicationRepository applicationRepository,
                                  UserRepository userRepository,
                                  InternshipRepository internshipRepository,
                                  AdminAccessService adminAccessService) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.internshipRepository = internshipRepository;
        this.adminAccessService = adminAccessService;
    }

    @Override
    public InternshipApplicationDto applyForInternship(Integer userId, Integer internshipId) {
        if (applicationRepository.existsByUserIdAndInternshipId(userId, internshipId)) {
            throw new UnauthorizedException("You have already applied for this internship.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Internship internship = internshipRepository.findById(internshipId)
                .orElseThrow(() -> new ResourceNotFoundException("Internship not found with id: " + internshipId));

        InternshipApplication application = new InternshipApplication();
        application.setUser(user);
        application.setInternship(internship);
        application.setAppliedAt(LocalDateTime.now());
        application.setStatus("APPLIED");
        return mapToDto(applicationRepository.save(application));
    }

    @Override
    public List<InternshipApplicationDto> getUserApplications(Integer userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return applicationRepository.findByUserIdOrderByAppliedAtDesc(userId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public List<InternshipApplicationDto> getAllApplicationsForAdmin(String adminPass) {
        adminAccessService.validateAdminPass(adminPass);
        return applicationRepository.findAllByOrderByAppliedAtDesc()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public InternshipApplicationDto updateApplicationStatusForAdmin(Integer applicationId, String status, String adminPass) {
        adminAccessService.validateAdminPass(adminPass);
        InternshipApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + applicationId));
        application.setStatus(status.trim().toUpperCase());
        return mapToDto(applicationRepository.save(application));
    }

    private InternshipApplicationDto mapToDto(InternshipApplication application) {
        InternshipApplicationDto dto = new InternshipApplicationDto();
        dto.setId(application.getId());
        dto.setUserId(application.getUser().getId());
        dto.setUserName(application.getUser().getName());
        dto.setUserEmail(application.getUser().getEmail());
        dto.setInternshipId(application.getInternship().getId());
        dto.setInternshipTitle(application.getInternship().getTitle());
        dto.setCompany(application.getInternship().getCompany());
        dto.setCategory(application.getInternship().getCategory());
        dto.setApplyLink(application.getInternship().getApplyLink());
        dto.setAppliedAt(application.getAppliedAt());
        dto.setStatus(application.getStatus() == null || application.getStatus().isBlank()
                ? "APPLIED"
                : application.getStatus());
        return dto;
    }
}
