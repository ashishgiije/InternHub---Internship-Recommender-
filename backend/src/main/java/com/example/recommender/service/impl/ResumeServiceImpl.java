package com.example.recommender.service.impl;

import com.example.recommender.dto.ResumeExtractResponse;
import com.example.recommender.exception.UnauthorizedException;
import com.example.recommender.service.ResumeService;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ResumeServiceImpl implements ResumeService {

    private static final List<String> KNOWN_SKILLS = List.of(
            "java", "spring boot", "python", "sql", "mysql", "postgresql", "javascript", "typescript",
            "react", "html", "css", "git", "docker", "aws", "machine learning", "tensorflow", "pandas",
            "excel", "node.js", "rest api");

    private static final List<String> INTEREST_KEYWORDS = List.of(
            "backend", "frontend", "full stack", "data", "analytics", "ai", "ml", "cloud", "devops", "security");

    @Override
    public ResumeExtractResponse extractResumeData(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new UnauthorizedException("Please upload a valid resume file.");
        }

        String text = readResumeText(file).toLowerCase(Locale.ROOT);
        Set<String> skills = new LinkedHashSet<>();
        Set<String> interests = new LinkedHashSet<>();

        for (String skill : KNOWN_SKILLS) {
            if (text.contains(skill)) {
                skills.add(skill);
            }
        }
        for (String interest : INTEREST_KEYWORDS) {
            if (text.contains(interest)) {
                interests.add(interest);
            }
        }

        ResumeExtractResponse response = new ResumeExtractResponse();
        response.setExtractedSkills(String.join(", ", skills));
        response.setExtractedInterests(String.join(", ", interests));
        response.setExtractedActivities(extractActivityLines(text));
        return response;
    }

    private String readResumeText(MultipartFile file) {
        String filename = file.getOriginalFilename() == null ? "" : file.getOriginalFilename().toLowerCase(Locale.ROOT);
        try {
            if (filename.endsWith(".pdf")) {
                try (PDDocument document = Loader.loadPDF(file.getBytes())) {
                    return new PDFTextStripper().getText(document);
                }
            }
            return new String(file.getBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UnauthorizedException("Unable to parse resume file.");
        }
    }

    private String extractActivityLines(String text) {
        String[] lines = text.split("\\r?\\n");
        StringBuilder activities = new StringBuilder();
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.length() < 20) {
                continue;
            }
            if (trimmed.contains("project") || trimmed.contains("experience") || trimmed.contains("intern")) {
                if (activities.length() > 0) {
                    activities.append(", ");
                }
                activities.append(trimmed);
            }
            if (activities.length() > 240) {
                break;
            }
        }
        return activities.toString();
    }
}
