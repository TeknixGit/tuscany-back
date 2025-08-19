package com.tuscany.tour.security.response;

import com.tuscany.tour.models.TourStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

// dto/tour/TourResponse.java
@Getter
@Setter
@Builder
public class TourResponse {
    private Long id;
    private String slug;
    private String title;
    private String description;
    private String date;
    private String category;
    private String location;
    private Double price;
    private String mainImage;
    private TourStatus status;
    private List<TourGalleryResponse> gallery; // ✅ Properly structured

    private TourDetailResponse details;

    private Integer capacity;

    private String duration;
}