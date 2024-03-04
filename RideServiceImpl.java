package com.example.SMS.service;

import com.example.SMS.dto.AddRideForm;
import com.example.SMS.entity.Ride;
import com.example.SMS.entity.Ride;
import com.example.SMS.entity.User;
import com.example.SMS.repository.RideRepository;
import com.example.SMS.repository.RideRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.text.ParseException;
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
        if (addRideForm.getId() != 0){
            Ride ride = rideRepository.getRideByUserAndId(user, addRideForm.getId())
                    .orElseThrow(() -> new Exception("Invalid Ride id"));

            ride.setTitle(addRideForm.getTitle());
            ride.setDescription(addRideForm.getDescription());
            ride.setFrom(addRideForm.getFrom());
            ride.setTo(addRideForm.getTo());

            ride.setDateTime(stringToTimestamp(addRideForm.getDateTime()));
            ride.setPrice(addRideForm.getPrice());
            rideRepository.save(ride);

        }else {
            Ride ride = Ride.builder()
                    .user(user)
                    .title(addRideForm.getTitle())
                    .description(addRideForm.getDescription())
                    .from(addRideForm.getFrom())
                    .to(addRideForm.getTo())
                    .dateTime(stringToTimestamp(addRideForm.getDateTime()))
                    .price(addRideForm.getPrice())
                    .postedDate(new Timestamp(Instant.now().toEpochMilli()))
                    .status("AVAILABLE")
                    .build();

            rideRepository.save(ride);
        }


    }

    @SneakyThrows
    public Timestamp stringToTimestamp(String datetimeString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            // Parse the datetime string to create a Date object
            Date date = sdf.parse(datetimeString);
            // Get the Unix timestamp (number of milliseconds since January 1, 1970)
            return new Timestamp(date.getTime() / 1000); // Convert milliseconds to seconds
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
        ride.setReservedBy(user);
        rideRepository.save(ride);
    }
}
