package com.tuscany.tour.security.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourDetailResponse {

    private Long id;

  //  private LocalDate date;

   // private LocalTime time;

    private String groupSize;

    //private String duration;

    private Boolean guideService;

    private String detailedDescription;

    private String language;

    private String entryFees;

    private String transportation;
    private Long tourId; // Assuming you want to include the tour ID in the response
}
