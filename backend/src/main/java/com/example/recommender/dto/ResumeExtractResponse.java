package com.example.recommender.dto;

public class ResumeExtractResponse {

    private String extractedSkills;
    private String extractedInterests;
    private String extractedActivities;

    public String getExtractedSkills() {
        return extractedSkills;
    }

    public void setExtractedSkills(String extractedSkills) {
        this.extractedSkills = extractedSkills;
    }

    public String getExtractedInterests() {
        return extractedInterests;
    }

    public void setExtractedInterests(String extractedInterests) {
        this.extractedInterests = extractedInterests;
    }

    public String getExtractedActivities() {
        return extractedActivities;
    }

    public void setExtractedActivities(String extractedActivities) {
        this.extractedActivities = extractedActivities;
    }
}
