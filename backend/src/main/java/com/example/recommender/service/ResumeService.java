package com.example.recommender.service;

import com.example.recommender.dto.ResumeExtractResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ResumeService {

    ResumeExtractResponse extractResumeData(MultipartFile file);
}
