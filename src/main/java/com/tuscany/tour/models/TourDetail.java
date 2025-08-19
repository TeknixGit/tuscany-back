package com.tuscany.tour.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;


@Entity
@Table(name = "tour_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class TourDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "detailed_description", columnDefinition = "TEXT")
    private String detailedDescription;

    @Column(name = "group_size")
    private String groupSize;

    @Column(name = "guide_service")
    private Boolean guideService;

    @Column(name = "language")
    private String language;

    @Column(name = "entry_fees")
    private String entryFees;

    @Column(name = "transportation")
    private String transportation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id")
    private Tour tour; // Assuming you have a Tour entity defined
    //private LocalDate date;
    //private LocalTime time;



    public boolean isEmpty() {
        return (detailedDescription == null || detailedDescription.isEmpty()) &&
               (groupSize == null || groupSize.isEmpty()) &&
               (guideService == null) &&
               (language == null || language.isEmpty()) &&
               (entryFees == null || entryFees.isEmpty()) &&
               (transportation == null || transportation.isEmpty());
    }
}
