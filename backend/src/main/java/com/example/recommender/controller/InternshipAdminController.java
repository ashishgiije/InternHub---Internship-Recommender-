package com.example.recommender.controller;

import com.example.recommender.dto.InternshipDto;
import com.example.recommender.dto.InternshipRequest;
import com.example.recommender.service.AdminAccessService;
import com.example.recommender.service.InternshipAdminService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/internships")
public class InternshipAdminController {

    private final InternshipAdminService internshipAdminService;
    private final AdminAccessService adminAccessService;

    public InternshipAdminController(InternshipAdminService internshipAdminService,
                                     AdminAccessService adminAccessService) {
        this.internshipAdminService = internshipAdminService;
        this.adminAccessService = adminAccessService;
    }

    @PostMapping
    public ResponseEntity<InternshipDto> addInternship(@RequestHeader("X-Admin-Pass") String adminPass,
                                                       @Valid @RequestBody InternshipRequest request) {
        adminAccessService.validateAdminPass(adminPass);
        InternshipDto created = internshipAdminService.addInternship(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<InternshipDto>> getAllInternships(@RequestHeader("X-Admin-Pass") String adminPass) {
        adminAccessService.validateAdminPass(adminPass);
        return ResponseEntity.ok(internshipAdminService.getAllInternships());
    }

    @PutMapping("/{id}")
    public ResponseEntity<InternshipDto> updateInternship(@RequestHeader("X-Admin-Pass") String adminPass,
                                                          @PathVariable Integer id,
                                                          @Valid @RequestBody InternshipRequest request) {
        adminAccessService.validateAdminPass(adminPass);
        return ResponseEntity.ok(internshipAdminService.updateInternship(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInternship(@RequestHeader("X-Admin-Pass") String adminPass,
                                                 @PathVariable Integer id) {
        adminAccessService.validateAdminPass(adminPass);
        internshipAdminService.deleteInternship(id);
        return ResponseEntity.noContent().build();
    }
}
