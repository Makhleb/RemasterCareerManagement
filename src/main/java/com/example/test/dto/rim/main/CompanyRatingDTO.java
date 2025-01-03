package com.example.test.dto.rim.main;

import lombok.Data;
import java.util.List;

@Data
public class CompanyRatingDTO {
    private double averageRating;
    private int reviewCount;
    private List<Integer> ratingDistribution; // [1점개수, 2점개수, ...]
    private List<ReviewDTO> recentReviews;
} 