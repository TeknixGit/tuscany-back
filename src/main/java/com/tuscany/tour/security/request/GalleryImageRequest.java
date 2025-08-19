package com.tuscany.tour.security.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

// dto/tour/GalleryImageRequest.java
@Getter
@Setter
public class GalleryImageRequest {
    @NotBlank
    private String imageUrl;
}
