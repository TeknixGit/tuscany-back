package com.tuscany.tour.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

// models/Tour.java
@Entity
@Table(name = "tours")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique = true)
    private String slug;                // for SEO / /tour/{slug}

    @Column(nullable=false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String duration;

    private String location;
    private Double price;
    private String mainImage;
    private String category;
    private Integer capacity;

    @Column
    private LocalDate date;

    @OneToOne(mappedBy = "tour", cascade = CascadeType.ALL)
    private TourDetail details;

    // Getter & Setter
    public void setDetails(TourDetail details) {
        this.details = details;
        details.setTour(this); // liaison bidirectionnelle
    }


    @Enumerated(EnumType.STRING)
    private TourStatus status; // ACTIVE, INACTIVE, DRAFT




    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TourGallery> gallery = new ArrayList<>();

    // optional: createdAt, updatedAt
}


