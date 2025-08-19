package com.tuscany.tour.security.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

// dto/tour/SearchRequest.java
@Getter
@Setter
public class SearchRequest {
    private String tourType;           // "public" or "private" (optional - you can ignore for now or map to category)
    private Integer numberOfPeople;    // adults
    private LocalDate date;
    private LocalTime time;
    private String tour;               // title contains
    private String transportation;     // transport contains
    private String location;           // optional
}
