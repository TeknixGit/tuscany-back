package com.tuscany.tour.repository;

import com.tuscany.tour.models.Tour;
import com.tuscany.tour.models.TourStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

// repository/TourRepository.java
public interface TourRepository extends JpaRepository<Tour, Long>, JpaSpecificationExecutor<Tour> {
    Optional<Tour> findBySlug(String slug);
    boolean existsBySlug(String slug);
    List<Tour> findTop8ByStatusOrderByIdDesc(TourStatus status);

        // Search by title, location, category, ignoring case and allowing partial match
        @Query("SELECT t FROM Tour t WHERE " +
                "LOWER(t.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                "LOWER(t.location) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
                "LOWER(t.category) LIKE LOWER(CONCAT('%', :query, '%'))")
        List<Tour> searchTours(@Param("query") String query);


}
