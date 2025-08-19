package com.tuscany.tour.services.impl;

import com.tuscany.tour.exceptions.NotFoundException;
import com.tuscany.tour.models.*;
import com.tuscany.tour.repository.TourDetailRepository;
import com.tuscany.tour.repository.TourGalleryRepository;
import com.tuscany.tour.repository.TourRepository;
import com.tuscany.tour.security.request.*;
import com.tuscany.tour.security.response.*;
import com.tuscany.tour.services.TourService;
import com.tuscany.tour.services.mapper.TourMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Transactional
@Service
public class TourServiceImpl implements TourService {

    private final TourRepository tourRepository;
    private final TourDetailRepository detailRepository;
    private final TourGalleryRepository galleryRepository;
    private final TourMapper mapper;

    @Value("${app.base-url}")
    private String baseUrl;
    @Override
    public TourResponse create(TourCreateRequest request) {
        if (tourRepository.existsBySlug(request.getSlug())) {
            throw new IllegalArgumentException("Slug already exists");
        }


        TourDetail tourDetail = new TourDetail();

        Tour tour = Tour.builder()
                .slug(request.getSlug())
                .title(request.getTitle())
                .description(request.getDescription())
                .details(tourDetail)
                //.gallery(tourGallery)
                .location(request.getLocation())
                .price(request.getPrice())
                .category(request.getCategory())
                .capacity(request.getCapacity())
                .duration(request.getDuration())
                //.date((request.getDate()))
                .mainImage(request.getMainImage())
                .status(request.getStatus())
                .build();

        tourRepository.save(tour);
        return mapper.toResponse(tour);
    }

    @Override
    public TourResponse update(Long id, TourUpdateRequest request) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tour not found"));

        if (request.getTitle() != null) tour.setTitle(request.getTitle());
        if (request.getDescription() != null) tour.setDescription(request.getDescription());
        if (request.getLocation() != null) tour.setLocation(request.getLocation());
        if (request.getPrice() != null) tour.setPrice(request.getPrice());
        if (request.getMainImage() != null) tour.setMainImage(request.getMainImage());
        if (request.getStatus() != null) tour.setStatus(request.getStatus());

        return mapper.toResponse(tour);
    }

    @Override
    public void delete(Long id) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tour not found"));
        tourRepository.delete(tour);
    }

    @Override
    @Transactional(readOnly = true)
    public TourResponse getById(Long id) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tour not found"));
        return mapper.toResponse(tour);
    }

    @Override
    @Transactional(readOnly = true)
    public TourResponse getBySlug(String slug) {
        Tour tour = tourRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Tour not found"));
        return mapper.toResponse(tour);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TourResponse> list(TourStatus status) {
        List<Tour> tours = (status == null)
                ? tourRepository.findAll()
                : tourRepository.findAll((root, query, cb) -> cb.equal(root.get("status"), status));
        // System.out.println("Listing tours with status: " + status);
        // if (tours.isEmpty()) {
        //     System.out.println("No tours found with status: " + status);
        // } else {
        //     System.out.println("Found " + tours.size() + " tours with status: " + status);
        //     for (Tour tour : tours) {
        //         System.out.println("Tour ID: " + tour.getId() + ", Title: " + tour.getTitle() +
        //                 ", Status: " + tour.getStatus());
        //     }
        // }
        return tours.stream().map(mapper::toResponse).toList();
    }


    @Override
    @Transactional(readOnly = true)
    public List<TourResponse> listPaged(int page, int size) {
        Page<Tour> p = tourRepository.findAll(PageRequest.of(page, size));
        return p.getContent().stream().map(mapper::toResponse).toList();
    }

    @Override
    public TourDetailResponse addDetail(Long tourId, TourDetailRequest request) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new NotFoundException("Tour not found"));

        TourDetail detail = TourDetail.builder()
                .tour(tour)

                .transportation(request.getTransportation())
                .groupSize(request.getGroupSize())
                //.duration(request.getDuration())
                //.location(request.getLocation())
                .detailedDescription(request.getDetailedDescription())
                
                .guideService(request.getGuideService())
                .language(request.getLanguage())
                .entryFees(request.getEntryFees())
                .build();

        // Sauvegarde du détail
        detailRepository.save(detail);

        // Attacher le détail au tour
        tour.setDetails(detail);
        tourRepository.save(tour);

        return mapper.toDetailResponse(detail);
    }

    @Override
    public void removeDetail(Long tourId, Long detailId) {
        TourDetail d = detailRepository.findById(detailId)
                .orElseThrow(() -> new NotFoundException("Detail not found"));
        if (!d.getTour().getId().equals(tourId)) {
            throw new IllegalArgumentException("Detail does not belong to tour");
        }
        d.getTour().setDetails(null); // Détache le lien
        detailRepository.delete(d);
    }

    @Override
    public List<String> addGalleryImages(Long tourId, List<GalleryImageRequest> images) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new NotFoundException("Tour not found"));

        List<TourGallery> toSave = images.stream()
                .map(req -> TourGallery.builder()
                        .tour(tour)
                        .imageUrl(req.getImageUrl())
                         .sourceType(SourceType.URL)

                        .build())
                .toList();

        galleryRepository.saveAll(toSave);
        return toSave.stream().map(TourGallery::getImageUrl).toList();
    }

    @Override
    public void removeGalleryImage(Long tourId, Long galleryId) {
        TourGallery g = galleryRepository.findById(galleryId)
                .orElseThrow(() -> new NotFoundException("Image not found"));
        if (!g.getTour().getId().equals(tourId)) {
            throw new IllegalArgumentException("Image does not belong to tour");
        }
        galleryRepository.delete(g);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SearchResponse> search(SearchRequest r) {
        List<TourDetail> details = detailRepository.search(null,  r.getTransportation());

        return details.stream()
                .filter(d -> {
                    Tour t = d.getTour();
                    boolean ok = true;
                    if (r.getTour() != null && !r.getTour().isBlank()) {
                        ok &= t.getTitle().toLowerCase().contains(r.getTour().toLowerCase());
                    }
                    if (r.getLocation() != null && !r.getLocation().isBlank()) {
                        ok &= t.getLocation() != null &&
                                t.getLocation().toLowerCase().contains(r.getLocation().toLowerCase());
                    }
                    return ok;
                })
                .map(d -> SearchResponse.builder()

                        .title(d.getTour().getTitle())
                        .location(d.getTour().getLocation())

                        .build())
                .toList();
    }

    public String storeGalleryImage(Long tourId, MultipartFile file) throws IOException {
        if (file.isEmpty() || !file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Invalid image file");
        }

        String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();
        Path uploadPath = Paths.get("uploads/gallery/");
        Files.createDirectories(uploadPath);
        Path filePath = uploadPath.resolve(filename);
        file.transferTo(filePath);

        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new NotFoundException("Tour not found"));

        String publicUrl =baseUrl+ "/uploads/gallery/" + filename;

        //galleryRepository.save(new TourGallery( publicUrl, tour));
        galleryRepository.save(new TourGallery( publicUrl, tour, SourceType.LOCAL));

        return publicUrl;
    }

    public String storeMainImage(Long tourId, MultipartFile file) throws IOException {
        if (file.isEmpty() || !file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Invalid image file");
        }

        String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();
        Path uploadPath = Paths.get("uploads/tours/");
        Files.createDirectories(uploadPath);
        Path filePath = uploadPath.resolve(filename);
        file.transferTo(filePath);

        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new NotFoundException("Tour not found"));

        String publicUrl =baseUrl+ "/uploads/tours/" + filename;

        //galleryRepository.save(new TourGallery( publicUrl, tour));
        tour.setMainImage(publicUrl);
        tourRepository.save(tour);
        //galleryRepository.save(new TourGallery( publicUrl, tour, SourceType.LOCAL));

        return publicUrl;
    }
    @Override
    public TourDetailResponse updateDetail(Long id, TourDetailRequest request) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tour not found"));

        TourDetail detail = detailRepository.findByTour_Id(tour.getId());
        if (detail == null) {
            // ⚠ Aucun détail n'existe encore, on le crée via addDetail
            return addDetail(id, request);
        }

        detail.setGroupSize(request.getGroupSize());

        //detail.setDuration(request.getDuration());
        detail.setGuideService(request.getGuideService());
        detail.setDetailedDescription(request.getDetailedDescription());
        detail.setLanguage(request.getLanguage());
        detail.setEntryFees(request.getEntryFees());
        detail.setTransportation(request.getTransportation());
        detail.setTour(tour);
        detailRepository.save(detail);

        return new TourDetailResponse(
                detail.getId(),

                detail.getGroupSize(),
                detail.getGuideService(),
                detail.getDetailedDescription(),
                detail.getLanguage(),
                detail.getEntryFees(),
                detail.getTransportation(),
                detail.getTour().getId() // Retourne l'ID du tour associé

        );
    }

    @Override
    public List<TourResponse> searchTour(SearchRequest request) {
        List<Tour> tours = tourRepository.findAll(); // En prod, il faut faire un vrai query dynamique

        return tours.stream()
                .filter(tour -> request.getTour() == null ||
                        tour.getTitle().toLowerCase().contains(request.getTour().toLowerCase()))
                 .filter(tour -> request.getLocation() == null ||
                        tour.getLocation().toLowerCase().contains(request.getLocation().toLowerCase()))
//                .filter(tour -> request.getTransportation() == null ||
//                        tour.getDetails().getTransportation().toLowerCase().contains(request.getTransportation().toLowerCase()))
////                .filter(tour -> request.getNumberOfPeople() == null ||
//                        tour.getDetails().getGroupSize() >= request.getNumberOfPeople())
               // .filter(tour -> request.getDate() == null || tour.getDate().equals(request.getDate()))
                //.filter(tour -> request.getTime() == null || tour.getDetails().getTime().equals(request.getTime()))
                .map(this::mapToTourResponse)
                .collect(Collectors.toList());
    }

    private TourResponse mapToTourResponse(Tour tour) {
        // affciher les détails du tour
        if (tour == null) {
            throw new NotFoundException("Tour not found");

        }else {
            // afficher
            System.out.println("Tour found: " + tour.getTitle());
            System.out.println("Tour ID: " + tour.getId());
            System.out.println("Tour Slug: " + tour.getSlug());
            System.out.println("Tour Description: " + tour.getDescription());
            System.out.println("Tour Date: " + tour.getDate());
            System.out.println("Tour Location: " + tour.getLocation());
            System.out.println("Tour Price: " + tour.getPrice());
            System.out.println("Tour Category: " + tour.getCategory());
            System.out.println("Tour Main Image: " + tour.getMainImage());
            System.out.println("Tour Status: " + tour.getStatus());
            System.out.println("Tour Capacity: " + tour.getCapacity());
            System.out.println("Tour Details: " + (tour.getDetails().getLanguage()));
            System.out.println("Tour Details: " + (tour.getDetails().getTransportation()));
            System.out.println("Tour Details: " + (tour.getDetails().getGroupSize()));
            System.out.println("Tour Details: " + (tour.getDetails().getGuideService()));
            System.out.println("Tour Details: " + (tour.getDetails().getEntryFees()));
            System.out.println("Tour Details: " + (tour.getDetails().getDetailedDescription()));
            System.out.println("Tour Details: " + (tour.getDetails().getTransportation()));
            System.out.println("Tour Details: " + (tour.getDetails().getId()));
        }
        return TourResponse.builder()
                .id(tour.getId())
                .title(tour.getTitle())
                .description(tour.getDescription())
                .slug(tour.getSlug())
                .date(String.valueOf(tour.getDate()))
                //.time(tour..getTime())
                .location(tour.getLocation())
                //.transportation(tour.getTransportation())
                //.numberOfPeople(tour.getNumberOfPeople())
                .price(tour.getPrice())
                .category(tour.getCategory())
                .mainImage(tour.getMainImage())
                .status(tour.getStatus())
                .capacity(tour.getCapacity())
                .details(tour.getDetails() != null ? mapper.toDetailResponse(tour.getDetails()) : null)

                //.thumbnailUrl(tour.getThumbnailUrl())
                .build();
    }

}
