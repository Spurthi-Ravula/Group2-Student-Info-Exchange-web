package com.example.SMS.service;

import com.example.SMS.dto.AddRideForm;
import com.example.SMS.entity.Ride;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RideService {
    List<Ride> getAvailableRides();

    void addRide(AddRideForm addRideForm);

    List<Ride> getUserRides();

    void deleteRide(Long RideId);

    void reserveRide(Long id);
}