package com.example.recommender.service.impl;

import com.example.recommender.dto.InternshipDto;
import com.example.recommender.dto.InternshipRequest;
import com.example.recommender.entity.Internship;
import com.example.recommender.exception.ResourceNotFoundException;
import com.example.recommender.repository.InternshipRepository;
import com.example.recommender.service.InternshipAdminService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class InternshipAdminServiceImpl implements InternshipAdminService {

    private final InternshipRepository internshipRepository;

    public InternshipAdminServiceImpl(InternshipRepository internshipRepository) {
        this.internshipRepository = internshipRepository;
    }

    @Override
    public InternshipDto addInternship(InternshipRequest request) {
        Internship saved = internshipRepository.save(mapToEntity(new Internship(), request));
        return mapToDto(saved);
    }

    @Override
    public List<InternshipDto> getAllInternships() {
        return internshipRepository.findAll().stream().map(this::mapToDto).toList();
    }

    @Override
    public InternshipDto updateInternship(Integer id, InternshipRequest request) {
        Internship internship = internshipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Internship not found with id: " + id));
        Internship updated = internshipRepository.save(mapToEntity(internship, request));
        return mapToDto(updated);
    }

    @Override
    public void deleteInternship(Integer id) {
        Internship internship = internshipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Internship not found with id: " + id));
        internshipRepository.delete(internship);
    }

    private Internship mapToEntity(Internship internship, InternshipRequest request) {
        internship.setTitle(request.getTitle().trim());
        internship.setCompany(request.getCompany().trim());
        internship.setSkills(request.getSkills().trim());
        internship.setCategory(request.getCategory().trim());
        internship.setApplyLink(request.getApplyLink().trim());
        return internship;
    }

    private InternshipDto mapToDto(Internship internship) {
        InternshipDto dto = new InternshipDto();
        dto.setId(internship.getId());
        dto.setTitle(internship.getTitle());
        dto.setCompany(internship.getCompany());
        dto.setSkills(internship.getSkills());
        dto.setCategory(internship.getCategory() == null || internship.getCategory().isBlank()
                ? "General"
                : internship.getCategory());
        dto.setApplyLink(internship.getApplyLink());
        return dto;
    }
}
