package com.example.SMS.service;

import com.example.SMS.dto.AddRideForm;
import com.example.SMS.entity.Ride;
import com.example.SMS.entity.User;
import com.example.SMS.repository.RideRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class RideServiceImpl implements RideService {

    private final UserService userService;
    private final RideRepository rideRepository;

    @Override
    public List<Ride> getAvailableRides() {
        User user = userService.findUserByEmail(userService.getLoggedInUserEmail());
        return rideRepository.findAllByUserNotAndStatusNot(user, "RESERVED");
    }

    @SneakyThrows
    @Override
    public void addRide(AddRideForm addRideForm) {
        User user = userService.findUserByEmail(userService.getLoggedInUserEmail());
        if (addRideForm.getId() != 0) {
            Ride ride = rideRepository.getRideByUserAndId(user, addRideForm.getId())
                    .orElseThrow(() -> new Exception("Invalid Ride id"));

            ride.setTitle(addRideForm.getTitle());
            ride.setDescription(addRideForm.getDescription());
            ride.setFrom(addRideForm.getFrom());
            ride.setTo(addRideForm.getTo());
            ride.setAvailableSeats(addRideForm.getAvailableSeats());
            ride.setDateTime(stringToTimestamp(addRideForm.getDateTime()));
            ride.setPrice(addRideForm.getPrice());
            rideRepository.save(ride);

        } else {
            Ride ride = Ride.builder()
                    .user(user)
                    .title(addRideForm.getTitle())
                    .description(addRideForm.getDescription())
                    .from(addRideForm.getFrom())
                    .to(addRideForm.getTo())
                    .dateTime(stringToTimestamp(addRideForm.getDateTime()))
                    .price(addRideForm.getPrice())
                    .availableSeats(addRideForm.getAvailableSeats())
                    .postedDate(new Timestamp(Instant.now().toEpochMilli()))
                    .status("AVAILABLE")
                    .build();

            rideRepository.save(ride);
        }

    }

    @SneakyThrows
    public static Timestamp stringToTimestamp(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date parsedDate = dateFormat.parse(dateString);
        return new Timestamp(parsedDate.getTime());
    }

    @Override
    @SneakyThrows
    public List<Ride> getUserRides() {
        User user = userService.findUserByEmail(userService.getLoggedInUserEmail());
        return rideRepository.findAllByUser(user);
    }

    @Override
    @SneakyThrows
    public void deleteRide(Long RideId) {
        User user = userService.findUserByEmail(userService.getLoggedInUserEmail());
        Ride ride = rideRepository.getRideByUserAndId(user, RideId)
                .orElseThrow(() -> new Exception("Invalid Ride id"));
        rideRepository.delete(ride);
    }

    @Override
    @SneakyThrows
    public void reserveRide(Long id) {
        User user = userService.findUserByEmail(userService.getLoggedInUserEmail());
        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> new Exception("Invalid Ride id"));
        ride.setStatus("RESERVED");
       // ride.setAvailableSeats(ride.getAvailableSeats()-1);
        ride.setReservedBy(user);
        rideRepository.save(ride);
    }

    @Override
    @SneakyThrows
    public List<Ride> getUserReservedRides() {
        User user = userService.findUserByEmail(userService.getLoggedInUserEmail());
        return rideRepository.findAllByReservedBy(user);
    }

    @Override
    @SneakyThrows
    public void cancelReservedRide(Long RideId) {
        User user = userService.findUserByEmail(userService.getLoggedInUserEmail());
        Ride ride = rideRepository.findById(RideId)
                .orElseThrow(() -> new Exception("Invalid Ride id"));

        if (user.getEmail().equalsIgnoreCase(ride.getReservedBy().getEmail())) {
            ride.setReservedBy(null);
            //ride.setAvailableSeats(ride.getAvailableSeats()+1);
            ride.setStatus("AVAILABLE");
        }

        rideRepository.save(ride);
    }
}
