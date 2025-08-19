package com.tuscany.tour.repository;

import com.tuscany.tour.models.TourGallery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// repository/TourGalleryRepository.java
public interface TourGalleryRepository extends JpaRepository<TourGallery, Long> {
    List<TourGallery> findByTourId(Long tourId);
}
