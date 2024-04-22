package com.example.SMS.repository;

import com.example.SMS.entity.Ride;
import com.example.SMS.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RideRepository extends JpaRepository<Ride, Long> {

    List<Ride> findAllByUserNotAndStatusNot(User user, String status);
    List<Ride> findAllByUser(User user);

    Optional<Ride> getRideByUserAndId(User user, Long id);
    
    List<Ride> findAllByReservedBy(User reservedBy);
}