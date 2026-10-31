package com.example.recommender.dto;

import jakarta.validation.constraints.NotBlank;

public class InternshipRequest {

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @NotBlank(message = "Company cannot be empty")
    private String company;

    @NotBlank(message = "Skills cannot be empty")
    private String skills;

    @NotBlank(message = "Category cannot be empty")
    private String category;

    @NotBlank(message = "Apply link cannot be empty")
    private String applyLink;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getApplyLink() {
        return applyLink;
    }

    public void setApplyLink(String applyLink) {
        this.applyLink = applyLink;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
