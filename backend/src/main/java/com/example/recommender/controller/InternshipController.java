package com.example.recommender.controller;

import com.example.recommender.dto.InternshipDto;
import com.example.recommender.entity.Internship;
import com.example.recommender.repository.InternshipRepository;
import java.util.List;
import java.util.Locale;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internships")
public class InternshipController {

    private final InternshipRepository internshipRepository;

    public InternshipController(InternshipRepository internshipRepository) {
        this.internshipRepository = internshipRepository;
    }

    @GetMapping
    public ResponseEntity<List<InternshipDto>> getInternships(@RequestParam(required = false) String search,
                                                              @RequestParam(required = false) String category) {
        List<Internship> internships = (category == null || category.isBlank())
                ? internshipRepository.findAll()
                : internshipRepository.findByCategoryIgnoreCase(category.trim());

        String normalizedSearch = search == null ? "" : search.trim().toLowerCase(Locale.ROOT);
        List<InternshipDto> result = internships.stream()
                .filter(internship -> normalizedSearch.isBlank() || matchesSearch(internship, normalizedSearch))
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(result);
    }

    private boolean matchesSearch(Internship internship, String query) {
        String category = internship.getCategory() == null ? "general" : internship.getCategory().toLowerCase(Locale.ROOT);
        return internship.getTitle().toLowerCase(Locale.ROOT).contains(query)
                || internship.getCompany().toLowerCase(Locale.ROOT).contains(query)
                || internship.getSkills().toLowerCase(Locale.ROOT).contains(query)
                || category.contains(query);
    }

    private InternshipDto toDto(Internship internship) {
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
