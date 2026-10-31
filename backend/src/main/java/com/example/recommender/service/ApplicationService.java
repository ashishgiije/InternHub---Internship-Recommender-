package com.example.recommender.service;

import com.example.recommender.dto.InternshipApplicationDto;
import java.util.List;

public interface ApplicationService {

    InternshipApplicationDto applyForInternship(Integer userId, Integer internshipId);

    List<InternshipApplicationDto> getUserApplications(Integer userId);

    List<InternshipApplicationDto> getAllApplicationsForAdmin(String adminPass);

    InternshipApplicationDto updateApplicationStatusForAdmin(Integer applicationId, String status, String adminPass);
}
