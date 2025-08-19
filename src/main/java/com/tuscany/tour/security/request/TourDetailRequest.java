package com.tuscany.tour.security.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TourDetailRequest {


   // private LocalDate date;


  //  private LocalTime time;

    private String groupSize;
    private String duration;
    private Boolean guideService; // ✅ Changed from String to Boolean for consistency

    private String detailedDescription;

    private String language;
    private String entryFees;
    private String transportation;

}
