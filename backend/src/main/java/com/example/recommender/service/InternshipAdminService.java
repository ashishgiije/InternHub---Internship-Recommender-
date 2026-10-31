package com.example.recommender.service;

import com.example.recommender.dto.InternshipDto;
import com.example.recommender.dto.InternshipRequest;
import java.util.List;

public interface InternshipAdminService {

    InternshipDto addInternship(InternshipRequest request);

    List<InternshipDto> getAllInternships();

    InternshipDto updateInternship(Integer id, InternshipRequest request);

    void deleteInternship(Integer id);
}
