package com.tuscany.tour.security.request;

import com.tuscany.tour.models.TourStatus;
import lombok.Getter;
import lombok.Setter;

// dto/tour/TourUpdateRequest.java
@Getter
@Setter
public class TourUpdateRequest {
    private String title;
    private String description;
    private String location;
    private Double price;
    private String mainImage;
    private TourStatus status;
    private String duration;
}
