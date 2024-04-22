package com.example.SMS.service;

import com.example.SMS.dto.AddAccommodationForm;
import com.example.SMS.entity.Accommodation;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AccommodationService {
    List<Accommodation> getAvailableAccomodations();

    void addAccommodation(AddAccommodationForm addAccommodationForm, MultipartFile file);

    List<Accommodation> getUserAccomodations();

    void deleteAccommodation(Long accommodationId);

    void reserveAccommodation(Long id);
    
    List<Accommodation> getUserReservedAccomodations();
    
    void cancelReservedAccommodation(Long accommodationId);
}