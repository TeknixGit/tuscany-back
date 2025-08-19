package com.tuscany.tour.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TourGallery  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2048)
    private String imageUrl; // URL or file path of the image

    @ManyToOne
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    // date and time of the image upload automatically set to current time on create objct
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SourceType sourceType;


    // constructor
    public TourGallery(String imageUrl, Tour tour, SourceType sourceType) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.tour = tour;
        this.sourceType = sourceType;
}}

