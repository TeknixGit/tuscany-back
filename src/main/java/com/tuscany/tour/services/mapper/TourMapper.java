package com.tuscany.tour.services.mapper;

import com.tuscany.tour.models.Tour;
import com.tuscany.tour.models.TourDetail;
import com.tuscany.tour.models.TourGallery;
import com.tuscany.tour.security.response.TourDetailResponse;
import com.tuscany.tour.security.response.TourGalleryResponse;
import com.tuscany.tour.security.response.TourResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class TourMapper {

    public TourResponse toResponse(Tour tour) {
        return TourResponse.builder()
                .id(tour.getId())
                .slug(tour.getSlug())
                .title(tour.getTitle())
                .description(tour.getDescription())
                .capacity(tour.getCapacity())
                .category(tour.getCategory())
                .date(tour.getDate() != null ? tour.getDate().toString() : null)
                .location(tour.getLocation())
                .duration(tour.getDuration())
                .price(tour.getPrice())
                .mainImage(tour.getMainImage())
                .status(tour.getStatus())
                .gallery(tour.getGallery() == null ? List.of()
                        : tour.getGallery().stream()
                        .map(this::toGalleryResponse)
                        .collect(Collectors.toList()))
                .details(tour.getDetails() == null ? null : toDetailResponse(tour.getDetails()))
                .build();
    }

    public TourDetailResponse toDetailResponse(TourDetail d) {
        return TourDetailResponse.builder()
                .id(d.getId())
                //.time(d.getTime())
                .transportation(d.getTransportation())
                .groupSize(d.getGroupSize())
                //.duration(d.getDuration())
                //.location(d.getLocation())
                .guideService(d.getGuideService())
                .language(d.getLanguage())
                .entryFees(d.getEntryFees())
                .detailedDescription(d.getDetailedDescription())
                .tourId(d.getTour() != null ? d.getTour().getId() : null)
                .build();
    }

    public TourGalleryResponse toGalleryResponse(TourGallery g) {
        return TourGalleryResponse.builder()
                .id(g.getId())
                .imageUrl(g.getImageUrl())
                .build();
    }
}
