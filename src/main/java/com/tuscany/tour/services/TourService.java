package com.tuscany.tour.services;

import com.tuscany.tour.models.TourStatus;
import com.tuscany.tour.security.request.*;
import com.tuscany.tour.security.response.SearchResponse;
import com.tuscany.tour.security.response.TourDetailResponse;
import com.tuscany.tour.security.response.TourResponse;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

// service/TourService.java
public interface TourService {
    TourResponse create(TourCreateRequest request);
    TourResponse update(Long id, TourUpdateRequest request);
    void delete(Long id);

    TourResponse getById(Long id);
    TourResponse getBySlug(String slug);

    List<TourResponse> list(TourStatus status);

    List<TourResponse> listPaged(int page, int size);

    TourDetailResponse addDetail(Long tourId, TourDetailRequest request);
    void removeDetail(Long tourId, Long detailId);

    List<String> addGalleryImages(Long tourId, List<GalleryImageRequest> images);
    void removeGalleryImage(Long tourId, Long galleryId);

    List<SearchResponse> search(SearchRequest request);

    String storeGalleryImage(Long id, MultipartFile file) throws Exception;
    String storeMainImage(Long id, MultipartFile file) throws Exception;

    TourDetailResponse updateDetail(Long id, TourDetailRequest request);

    List<TourResponse> searchTour(@Valid SearchRequest request);

}
