package com.example.SMS.service;

import com.example.SMS.dto.AddMarketPlaceForm;
import com.example.SMS.entity.MarketPlace;
import com.example.SMS.entity.User;
import com.example.SMS.repository.MarketPlaceRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class MarketPlaceServiceImpl implements MarketPlaceService {

    private final MarketPlaceRepository marketPlaceRepository;
    private final UserService userService;


    @Override
    public List<MarketPlace> getUserMarketPlace() {
        User user = userService.findUserByEmail(userService.getLoggedInUserEmail());
        return marketPlaceRepository.findAllByUser(user);
    }

    @Override
    @SneakyThrows
    public void addMarketPlace(AddMarketPlaceForm addMarketPlaceForm, MultipartFile file) {
        User user = userService.findUserByEmail(userService.getLoggedInUserEmail());
        if (addMarketPlaceForm.getId() != 0){
            MarketPlace marketPlace = marketPlaceRepository.getMarketPlaceByUserAndId(user, addMarketPlaceForm.getId())
                    .orElseThrow(() -> new Exception("Invalid Market place id"));

            marketPlace.setTitle(addMarketPlaceForm.getTitle());
            marketPlace.setDescription(addMarketPlaceForm.getDescription());
            marketPlace.setAddress(addMarketPlaceForm.getAddress());
            marketPlace.setPrice(addMarketPlaceForm.getPrice());
            if (!file.isEmpty())
                marketPlace.setImage(file.getBytes());

            marketPlaceRepository.save(marketPlace);

        }else {
            MarketPlace marketPlace = MarketPlace.builder()
                    .user(user)
                    .title(addMarketPlaceForm.getTitle())
                    .description(addMarketPlaceForm.getDescription())
                    .address(addMarketPlaceForm.getAddress())
                    .price(addMarketPlaceForm.getPrice())
                    .postedDate(new Timestamp(Instant.now().toEpochMilli()))
                    .status("AVAILABLE")
                    .build();

            if (!file.isEmpty())
                marketPlace.setImage(file.getBytes());
            else throw new Exception("Please select image");

            marketPlaceRepository.save(marketPlace);
        }

    }

    @Override
    @SneakyThrows
    public void deleteMarketPlace(Long marketPlaceId) {
        User user = userService.findUserByEmail(userService.getLoggedInUserEmail());
        MarketPlace marketPlace = marketPlaceRepository.getMarketPlaceByUserAndId(user, marketPlaceId)
                .orElseThrow(() -> new Exception("Invalid Accommodation id"));
        marketPlaceRepository.delete(marketPlace);

    }

    @Override
    @SneakyThrows
    public void reserveMarketPlace(Long id) {
        User user = userService.findUserByEmail(userService.getLoggedInUserEmail());
        MarketPlace marketPlace = marketPlaceRepository.findById(id)
                .orElseThrow(() -> new Exception("Invalid Accommodation id"));
        marketPlace.setStatus("RESERVED");
        marketPlace.setReservedBy(user);
        marketPlaceRepository.save(marketPlace);
    }

    @Override
    public List<MarketPlace> getAvailableMarketPlaces() {
        User user = userService.findUserByEmail(userService.getLoggedInUserEmail());
        return marketPlaceRepository.findAllByUserNotAndStatusNot(user, "RESERVED");
    }
    
    @Override
    public List<MarketPlace> getUserReservedMarketPlace() {
        User user = userService.findUserByEmail(userService.getLoggedInUserEmail());
        return marketPlaceRepository.findAllByReservedBy(user);
    }
    
    @Override
    @SneakyThrows
    public void cancelReservedMarketPlace(Long marketPlaceId) {
        User user = userService.findUserByEmail(userService.getLoggedInUserEmail());
        MarketPlace marketPlace = marketPlaceRepository.findById(marketPlaceId)
                .orElseThrow(() -> new Exception("Invalid marketplace id"));
        
        if(user.getEmail().equalsIgnoreCase(marketPlace.getReservedBy().getEmail())) {
        	marketPlace.setReservedBy(null);
        	marketPlace.setStatus("AVAILABLE");
        }
        
        marketPlaceRepository.save(marketPlace);

    }
}
