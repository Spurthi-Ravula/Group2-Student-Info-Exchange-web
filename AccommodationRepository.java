package com.example.SMS.repository;

import com.example.SMS.entity.Accommodation;
import com.example.SMS.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {

    List<Accommodation> findAllByUserNotAndStatusNot(User user, String status);
    List<Accommodation> findAllByUser(User user);

    Optional<Accommodation> getAccommodationByUserAndId(User user, Long id);
    
    List<Accommodation> findAllByReservedBy(User reservedBy);
}