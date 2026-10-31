package com.example.recommender.dto;

import jakarta.validation.constraints.NotBlank;

public class ApplicationStatusUpdateRequest {

    @NotBlank(message = "Status cannot be empty")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
