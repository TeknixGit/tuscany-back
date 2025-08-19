package com.tuscany.tour.security.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

// dto/tour/SearchResponse.java
@Getter
@Setter
@Builder
public class SearchResponse {
    private Long tourId;
    private String tourSlug;
    private String title;
    private Double price;
    private String location;
    private String mainImage;
    private LocalDate date;
    private LocalTime time;
    private String transportation;
    private Integer maxPeople;
}
