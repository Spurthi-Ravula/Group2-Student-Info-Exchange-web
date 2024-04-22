package com.example.SMS.service;

import com.example.SMS.dto.AddRideForm;
import com.example.SMS.entity.Ride;

import java.util.List;

public interface RideService {
    List<Ride> getAvailableRides();

    void addRide(AddRideForm addRideForm);

    List<Ride> getUserRides();

    void deleteRide(Long RideId);

    void reserveRide(Long id);

	List<Ride> getUserReservedRides();
	
	void cancelReservedRide(Long RideId);
}