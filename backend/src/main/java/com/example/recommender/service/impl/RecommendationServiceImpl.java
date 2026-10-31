package com.example.recommender.service.impl;

import com.example.recommender.dto.RecommendationItem;
import com.example.recommender.dto.RecommendationRequest;
import com.example.recommender.dto.RecommendationResponse;
import com.example.recommender.entity.Internship;
import com.example.recommender.repository.InternshipRepository;
import com.example.recommender.service.RecommendationService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private final InternshipRepository internshipRepository;

    public RecommendationServiceImpl(InternshipRepository internshipRepository) {
        this.internshipRepository = internshipRepository;
    }

    @Override
    public RecommendationResponse recommendInternships(RecommendationRequest request) {
        List<String> userSkills = normalizeSkills(request.getSkills());
        List<String> userInterests = normalizeSkills(request.getInterests());
        List<Internship> internships = internshipRepository.findAll();

        List<RecommendationItem> recommendationItems = new ArrayList<>();
        Set<String> suggestedSkills = new LinkedHashSet<>();

        for (Internship internship : internships) {
            List<String> requiredSkills = normalizeSkills(internship.getSkills());
            if (requiredSkills.isEmpty()) {
                continue;
            }

            long matchedCount = requiredSkills.stream().filter(userSkills::contains).count();
            double score = (matchedCount * 100.0) / requiredSkills.size();
            // Add a small bonus when internship text aligns with user interests.
            score = applyInterestBonus(score, internship, userInterests);

            List<String> missingSkills = requiredSkills.stream()
                    .filter(skill -> !userSkills.contains(skill))
                    .toList();

            suggestedSkills.addAll(missingSkills);

            RecommendationItem item = new RecommendationItem();
            item.setInternshipId(internship.getId());
            item.setTitle(internship.getTitle());
            item.setCompany(internship.getCompany());
            item.setRequiredSkills(internship.getSkills());
            item.setApplyLink(internship.getApplyLink());
            item.setMatchingScore(Math.round(score * 100.0) / 100.0);
            item.setMissingSkills(missingSkills);
            recommendationItems.add(item);
        }

        recommendationItems.sort(Comparator.comparingDouble(RecommendationItem::getMatchingScore).reversed());

        RecommendationResponse response = new RecommendationResponse();
        response.setRecommendations(recommendationItems);
        response.setSuggestedSkills(suggestedSkills);
        return response;
    }

    private List<String> normalizeSkills(String rawSkills) {
        if (rawSkills == null) {
            return List.of();
        }
        return Arrays.stream(rawSkills.split(","))
                .map(String::trim)
                .filter(skill -> !skill.isEmpty())
                .map(skill -> skill.toLowerCase(Locale.ROOT))
                .distinct()
                .toList();
    }

    private double applyInterestBonus(double baseScore, Internship internship, List<String> userInterests) {
        if (userInterests.isEmpty()) {
            return Math.min(baseScore, 100.0);
        }

        String searchableText = String.join(" ",
                        internship.getTitle(),
                        internship.getCompany(),
                        internship.getSkills())
                .toLowerCase(Locale.ROOT);

        long interestMatches = userInterests.stream().filter(searchableText::contains).count();
        if (interestMatches == 0) {
            return Math.min(baseScore, 100.0);
        }

        double bonus = Math.min(10.0, (interestMatches * 10.0) / userInterests.size());
        return Math.min(baseScore + bonus, 100.0);
    }
}
