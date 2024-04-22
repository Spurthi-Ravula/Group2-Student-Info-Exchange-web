package com.example.SMS.repository;

import com.example.SMS.entity.Event;
import com.example.SMS.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByUserNotAndAvailableSeatsGreaterThanAndUsersNotContaining(User user, int availableSeats, User user1);
    List<Event> findAllByUser(User user);

    Optional<Event> getEventByUserAndId(User user, Long id);

    Set<Event> findByUsers(User user);
}