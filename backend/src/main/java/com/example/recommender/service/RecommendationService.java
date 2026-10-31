package com.example.recommender.service;

import com.example.recommender.dto.RecommendationRequest;
import com.example.recommender.dto.RecommendationResponse;

public interface RecommendationService {

    RecommendationResponse recommendInternships(RecommendationRequest request);
}
