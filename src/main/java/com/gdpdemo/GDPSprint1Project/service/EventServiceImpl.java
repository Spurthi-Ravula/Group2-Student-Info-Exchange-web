package com.example.SMS.service;

import com.example.SMS.dto.AddEventForm;
import com.example.SMS.entity.Event;
import com.example.SMS.entity.User;
import com.example.SMS.repository.EventRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {

    private final UserService userService;
    private final EventRepository eventRepository;

    @Override
    public List<Event> getAvailableEvents() {
        User user = userService.findUserByEmail(userService.getLoggedInUserEmail());
        return eventRepository.findAllByUserNotAndAvailableSeatsGreaterThanAndUsersNotContaining(user, 0, user);
    }

    @SneakyThrows
    @Override
    public void addEvent(AddEventForm addEventForm) {
        User user = userService.findUserByEmail(userService.getLoggedInUserEmail());
        if (addEventForm.getId() != 0){
            Event event = eventRepository.getEventByUserAndId(user, addEventForm.getId())
                    .orElseThrow(() -> new Exception("Invalid Event id"));

            event.setTitle(addEventForm.getTitle());
            event.setDescription(addEventForm.getDescription());
            event.setAddress(addEventForm.getAddress());
            event.setAvailableSeats(addEventForm.getAvailableSeats());

            event.setDateTime(stringToTimestamp(addEventForm.getDateTime()));
            event.setPrice(addEventForm.getPrice());
            eventRepository.save(event);

        }else {
            Event event = Event.builder()
                    .user(user)
                    .title(addEventForm.getTitle())
                    .description(addEventForm.getDescription())
                    .address(addEventForm.getAddress())
                    .availableSeats(addEventForm.getAvailableSeats())
                    .dateTime(stringToTimestamp(addEventForm.getDateTime()))
                    .price(addEventForm.getPrice())
                    .postedDate(new Timestamp(Instant.now().toEpochMilli()))
                    .build();

            eventRepository.save(event);
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
    public List<Event> getUserEvents() {
        User user = userService.findUserByEmail(userService.getLoggedInUserEmail());
        return eventRepository.findAllByUser(user);
    }

    @Override
    @SneakyThrows
    public Set<Event> getUserReservedEvents() {
        User user = userService.findUserByEmail(userService.getLoggedInUserEmail());
        return eventRepository.findByUsers(user);
    }

    @Override
    @SneakyThrows
    public void deleteEvent(Long eventId) {
        User user = userService.findUserByEmail(userService.getLoggedInUserEmail());
        Event event = eventRepository.getEventByUserAndId(user, eventId)
                .orElseThrow(() -> new Exception("Invalid Event id"));
        eventRepository.delete(event);
    }

    @Override
    @SneakyThrows
    public void reserveEvent(Long id) {
        User user = userService.findUserByEmail(userService.getLoggedInUserEmail());
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new Exception("Invalid Event id"));
        event.setAvailableSeats(event.getAvailableSeats() - 1);
        event.getUsers().add(user);
        eventRepository.save(event);
    }
}
