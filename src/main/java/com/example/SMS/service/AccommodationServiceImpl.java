package com.example.SMS.service;

import com.example.SMS.dto.AddAccommodationForm;
import com.example.SMS.entity.Accommodation;
import com.example.SMS.entity.User;
import com.example.SMS.repository.AccommodationRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {

    private final UserService userService;
    private final AccommodationRepository accommodationRepository;

    @Override
    public List<Accommodation> getAvailableAccomodations() {
        User user = userService.findUserByEmail(userService.getLoggedInUserEmail());
        return accommodationRepository.findAllByUserNotAndStatusNot(user, "RESERVED");
    }

    @SneakyThrows
    @Override
    public void addAccommodation(AddAccommodationForm addAccommodationForm, MultipartFile file) {
        User user = userService.findUserByEmail(userService.getLoggedInUserEmail());
        if (addAccommodationForm.getId() != 0){
            Accommodation accommodation = accommodationRepository.getAccommodationByUserAndId(user, addAccommodationForm.getId())
                    .orElseThrow(() -> new Exception("Invalid Accommodation id"));

            accommodation.setTitle(addAccommodationForm.getTitle());
            accommodation.setDescription(addAccommodationForm.getDescription());
            accommodation.setAddress(addAccommodationForm.getAddress());
            accommodation.setAvailableFrom(stringToTimestamp(addAccommodationForm.getAvailableFrom()));
            accommodation.setPrice(addAccommodationForm.getPrice());
            if (!file.isEmpty())
                accommodation.setImage(file.getBytes());

            accommodationRepository.save(accommodation);

        }else {
            Accommodation accommodation = Accommodation.builder()
                    .user(user)
                    .title(addAccommodationForm.getTitle())
                    .description(addAccommodationForm.getDescription())
                    .address(addAccommodationForm.getAddress())
                    .availableFrom(stringToTimestamp(addAccommodationForm.getAvailableFrom()))
                    .price(addAccommodationForm.getPrice())
                    .postedDate(new Timestamp(Instant.now().toEpochMilli()))
                    .status("AVAILABLE")
                    .build();

            if (!file.isEmpty())
                accommodation.setImage(file.getBytes());
            else throw new Exception("Please select image");

            accommodationRepository.save(accommodation);
        }

    }

    @Override
    @SneakyThrows
    public List<Accommodation> getUserAccomodations() {
        User user = userService.findUserByEmail(userService.getLoggedInUserEmail());
        return accommodationRepository.findAllByUser(user);
    }

    @Override
    @SneakyThrows
    public void deleteAccommodation(Long accommodationId) {
        User user = userService.findUserByEmail(userService.getLoggedInUserEmail());
        Accommodation accommodation = accommodationRepository.getAccommodationByUserAndId(user, accommodationId)
                .orElseThrow(() -> new Exception("Invalid Accommodation id"));
        accommodationRepository.delete(accommodation);
    }

    @Override
    @SneakyThrows
    public void reserveAccommodation(Long id) {
        User user = userService.findUserByEmail(userService.getLoggedInUserEmail());
        Accommodation accommodation = accommodationRepository.findById(id)
                .orElseThrow(() -> new Exception("Invalid Accommodation id"));
        accommodation.setStatus("RESERVED");
        accommodation.setReservedBy(user);
        accommodationRepository.save(accommodation);
    }
    
    @Override
    @SneakyThrows
    public List<Accommodation> getUserReservedAccomodations() {
        User user = userService.findUserByEmail(userService.getLoggedInUserEmail());
        return accommodationRepository.findAllByReservedBy(user);
    }
    
    @Override
    @SneakyThrows
    public void cancelReservedAccommodation(Long accommodationId) {
        User user = userService.findUserByEmail(userService.getLoggedInUserEmail());
        Accommodation accommodation = accommodationRepository.findById(accommodationId)
                .orElseThrow(() -> new Exception("Invalid Accommodation id"));
        
        if(user.getEmail().equalsIgnoreCase(accommodation.getReservedBy().getEmail())) {
        	accommodation.setReservedBy(null);
        	accommodation.setStatus("AVAILABLE");
        }
        accommodationRepository.save(accommodation);
    }
    
    @SneakyThrows
    public static Timestamp stringToTimestamp(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date parsedDate = dateFormat.parse(dateString);
        return new Timestamp(parsedDate.getTime());
    }
}
