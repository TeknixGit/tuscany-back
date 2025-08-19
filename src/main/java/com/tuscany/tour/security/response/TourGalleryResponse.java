package com.tuscany.tour.security.response;

import com.tuscany.tour.models.SourceType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TourGalleryResponse {
    private Long id;
    private String imageUrl;
    private SourceType sourceType; // Assuming SourceType is a String representation
}
