package com.example.SMS.repository;

import com.example.SMS.entity.MarketPlace;
import com.example.SMS.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MarketPlaceRepository extends JpaRepository<MarketPlace, Long> {

    List<MarketPlace> findAllByUserNotAndStatusNot(User user, String status);
    List<MarketPlace> findAllByUser(User user);

    Optional<MarketPlace> getMarketPlaceByUserAndId(User user, Long id);
    List<MarketPlace> findAllByReservedBy(User reservedBy);
}