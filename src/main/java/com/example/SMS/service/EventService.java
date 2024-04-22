package com.example.SMS.service;

import com.example.SMS.dto.AddEventForm;
import com.example.SMS.entity.Event;

import java.util.List;
import java.util.Set;

public interface EventService {
    List<Event> getAvailableEvents();

    void addEvent(AddEventForm addEventForm);

    List<Event> getUserEvents();

    Set<Event> getUserReservedEvents();

    void deleteEvent(Long EventId);

    void reserveEvent(Long id);
    
    void cancelReservedEvent(Long EventId);
}