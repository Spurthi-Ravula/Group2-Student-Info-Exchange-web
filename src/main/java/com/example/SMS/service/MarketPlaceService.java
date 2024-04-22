package com.example.SMS.service;

import com.example.SMS.dto.AddMarketPlaceForm;
import com.example.SMS.entity.MarketPlace;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MarketPlaceService {

    List<MarketPlace> getUserMarketPlace();

    void addMarketPlace(AddMarketPlaceForm addMarketPlaceForm, MultipartFile file);

    void deleteMarketPlace(Long marketPlaceId);

    void reserveMarketPlace(Long id);

    List<MarketPlace> getAvailableMarketPlaces();
    
    List<MarketPlace> getUserReservedMarketPlace();
    
    void cancelReservedMarketPlace(Long marketPlaceId);
}