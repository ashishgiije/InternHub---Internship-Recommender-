package com.example.recommender.controller;

import com.example.recommender.dto.InternshipApplicationDto;
import com.example.recommender.dto.ApplicationStatusUpdateRequest;
import com.example.recommender.service.AdminAccessService;
import com.example.recommender.service.ApplicationService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminAccessService adminAccessService;
    private final ApplicationService applicationService;

    public AdminController(AdminAccessService adminAccessService, ApplicationService applicationService) {
        this.adminAccessService = adminAccessService;
        this.applicationService = applicationService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestHeader("X-Admin-Pass") String adminPass) {
        adminAccessService.validateAdminPass(adminPass);
        return ResponseEntity.ok(Map.of("message", "Admin login successful."));
    }

    @GetMapping("/applications")
    public ResponseEntity<List<InternshipApplicationDto>> getAllApplications(
            @RequestHeader("X-Admin-Pass") String adminPass) {
        return ResponseEntity.ok(applicationService.getAllApplicationsForAdmin(adminPass));
    }

    @PutMapping("/applications/{applicationId}/status")
    public ResponseEntity<InternshipApplicationDto> updateApplicationStatus(
            @PathVariable Integer applicationId,
            @RequestHeader("X-Admin-Pass") String adminPass,
            @Valid @RequestBody ApplicationStatusUpdateRequest request) {
        return ResponseEntity.ok(applicationService.updateApplicationStatusForAdmin(
                applicationId,
                request.getStatus(),
                adminPass
        ));
    }
}
