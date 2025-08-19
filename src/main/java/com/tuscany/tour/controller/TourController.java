package com.tuscany.tour.controller;

import com.tuscany.tour.models.TourStatus;
import com.tuscany.tour.security.request.*;
import com.tuscany.tour.security.response.SearchResponse;
import com.tuscany.tour.security.response.TourDetailResponse;
import com.tuscany.tour.security.response.TourResponse;
import com.tuscany.tour.services.TourService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// controller/TourController.java
@RestController
@RequestMapping("/api/tours")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class TourController {

    private final TourService tourService;

    // ---------- CRUD Tours ----------
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TourResponse> create(@Valid @RequestBody TourCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tourService.create(request));
    }
    @PostMapping("/{tourId}/main-image/upload")
    public ResponseEntity<String> uploadMainImage(
            @PathVariable Long tourId,
            @RequestParam("file") MultipartFile file) {

        try {
            String imageUrl = tourService.storeMainImage(tourId, file);
            return ResponseEntity.ok(imageUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image");
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TourResponse> update(@PathVariable Long id,
                                               @Valid @RequestBody TourUpdateRequest request) {
        return ResponseEntity.ok(tourService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tourService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ---------- READ ----------
    @GetMapping
    public ResponseEntity<List<TourResponse>> list(@RequestParam(required = false) TourStatus status) {
        return ResponseEntity.ok(tourService.list(status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TourResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(tourService.getById(id));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<TourResponse> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(tourService.getBySlug(slug));
    }

    // ---------- Details ----------
    @PostMapping("/{id}/details")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TourDetailResponse> addDetail(@PathVariable Long id,
                                                        @Valid @RequestBody TourDetailRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tourService.addDetail(id, request));
    }
    @PutMapping("/{id}/details")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TourDetailResponse> updateDetail(@PathVariable Long id,
                                                           @Valid @RequestBody TourDetailRequest request) {
        return ResponseEntity.ok(tourService.updateDetail(id, request));
    }


    @DeleteMapping("/{tourId}/details/{detailId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteDetail(@PathVariable Long tourId, @PathVariable Long detailId) {
        tourService.removeDetail(tourId, detailId);
        return ResponseEntity.noContent().build();
    }

    // ---------- Gallery ----------
    @PostMapping("/{id}/gallery")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<String>> addGallery(@PathVariable Long id,
                            @Valid @RequestBody List<GalleryImageRequest> images) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tourService.addGalleryImages(id, images));
    }
    // Nouveau endpoint pour upload de fichier image
    @PostMapping("/{id}/gallery/upload")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> uploadGalleryImage(@PathVariable Long id,
                                                     @RequestPart("file") MultipartFile file) throws Exception {
        String imageUrl = tourService.storeGalleryImage(id, file);
       // System.out.println(imageUrl);
        // Retourne l’URL publique de l’image créée
        return ResponseEntity.status(HttpStatus.CREATED).body(imageUrl);
    }


    @DeleteMapping("/{tourId}/gallery/{galleryId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteGalleryImage(@PathVariable Long tourId,
                                                   @PathVariable Long galleryId) {
        tourService.removeGalleryImage(tourId, galleryId);
        return ResponseEntity.noContent().build();
    }

    // ---------- Search (for SearchWidget) ----------
    @PostMapping("/search")
    public ResponseEntity<List<SearchResponse>> search(@Valid @RequestBody SearchRequest request) {
        return ResponseEntity.ok(tourService.search(request));
    }

    @PostMapping("/search-tour")
    public ResponseEntity<List<TourResponse>> searchtour(@Valid @RequestBody SearchRequest request) {
        return ResponseEntity.ok(tourService.searchTour(request));
    }
}
