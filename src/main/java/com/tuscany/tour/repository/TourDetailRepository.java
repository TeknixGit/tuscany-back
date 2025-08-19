package com.tuscany.tour.repository;

import com.tuscany.tour.models.TourDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

// repository/TourDetailRepository.java
public interface TourDetailRepository extends JpaRepository<TourDetail, Long> {
    TourDetail findByTour_Id(Long tourId);

    @Query("""
       SELECT d FROM TourDetail d
       WHERE (:tourId IS NULL OR d.tour.id = :tourId)
       
       
       
         AND (:transportation IS NULL OR LOWER(d.transportation) LIKE LOWER(CONCAT('%', :transportation, '%')))
    """)
    List<TourDetail> search(
        @Param("tourId") Long tourId,
        @Param("transportation") String transportation
    );
}
