package com.example.recommender.dto;

import java.util.List;
import java.util.Set;

public class RecommendationResponse {

    private List<RecommendationItem> recommendations;
    private Set<String> suggestedSkills;

    public List<RecommendationItem> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<RecommendationItem> recommendations) {
        this.recommendations = recommendations;
    }

    public Set<String> getSuggestedSkills() {
        return suggestedSkills;
    }

    public void setSuggestedSkills(Set<String> suggestedSkills) {
        this.suggestedSkills = suggestedSkills;
    }
}
