package com.tuscany.tour.security.request;

import com.tuscany.tour.models.TourStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

// dto/tour/TourCreateRequest.java
@Getter
@Setter
public class TourCreateRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String slug;
    private String description;
    private String location;
    private Integer capacity;
    @NotNull
    private Double price;
    private String mainImage;
    private TourStatus status = TourStatus.DRAFT;
    private String category; // Ajouté
    //private LocalDate date; // Ajouté
    //private LocalTime time;
    private String duration;

}
