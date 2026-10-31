package com.example.recommender.dto;

import jakarta.validation.constraints.NotBlank;

public class RecommendationRequest {

    @NotBlank(message = "Skills cannot be empty")
    private String skills;

    @NotBlank(message = "Interests cannot be empty")
    private String interests;

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }
}
