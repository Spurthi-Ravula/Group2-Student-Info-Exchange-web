package com.example.SMS.controller;

import com.example.SMS.dto.*;
import com.example.SMS.entity.*;
import com.example.SMS.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Controller
public class StudentController {

    @Autowired
    UserService userService;
    @Autowired
    AccommodationService accommodationService;
    @Autowired
    MarketPlaceService marketPlaceService;
    @Autowired
    RideService rideService;
    @Autowired
    EventService eventService;

    @GetMapping("/chat")
    public String showRegistrationForm(Model model) {
        User user = userService.findUserByEmail(userService.getLoggedInUserEmail());
        model.addAttribute("user", user);
        return "Live-chat";
    }

    @GetMapping("/my-accommodation")
    public String myAccommodations(Model model) {
        List<Accommodation> accommodations = accommodationService.getUserAccomodations();
        List<Accommodation> reservedAccommodations = accommodationService.getUserReservedAccomodations();
        model.addAttribute("reservedAccommodations", reservedAccommodations);
        model.addAttribute("accommodations", accommodations);
        model.addAttribute("success", "");
        model.addAttribute("message", "");

        return "my-accommodation";
    }


    @PostMapping("/add-accommodation")
    public String addProduct(@ModelAttribute(name = "AddAccommodationForm") AddAccommodationForm addAccommodationForm,
                             @RequestParam("file") MultipartFile file,
                             Model model) {
        try {
            accommodationService.addAccommodation(addAccommodationForm, file);
            List<Accommodation> accommodations = accommodationService.getUserAccomodations();
            List<Accommodation> reservedAccommodations = accommodationService.getUserReservedAccomodations();
            model.addAttribute("reservedAccommodations", reservedAccommodations);
            model.addAttribute("accommodations", accommodations);
            model.addAttribute("success", true);
            model.addAttribute("message", "Accommodation added/updated successfully");
            return "my-accommodation";

        } catch (Exception e) {
            List<Accommodation> accommodations = accommodationService.getUserAccomodations();
            List<Accommodation> reservedAccommodations = accommodationService.getUserReservedAccomodations();
            model.addAttribute("reservedAccommodations", reservedAccommodations);
            model.addAttribute("accommodations", accommodations);
            model.addAttribute("success", false);
            model.addAttribute("message", e.getMessage());
            return "my-accommodation";
        }
    }

    @GetMapping("/delete-accommodation")
    public String deleteAccommodation(@RequestParam(name = "accommodationId") Long accommodationId, Model model) {
        try {
            accommodationService.deleteAccommodation(accommodationId);
            List<Accommodation> accommodations = accommodationService.getUserAccomodations();
            List<Accommodation> reservedAccommodations = accommodationService.getUserReservedAccomodations();
            model.addAttribute("reservedAccommodations", reservedAccommodations);
            model.addAttribute("accommodations", accommodations);
            model.addAttribute("success", true);
            model.addAttribute("message", "Accommodation deleted successfully");
            return "my-accommodation";

        } catch (Exception e) {
            List<Accommodation> accommodations = accommodationService.getUserAccomodations();
            List<Accommodation> reservedAccommodations = accommodationService.getUserReservedAccomodations();
            model.addAttribute("reservedAccommodations", reservedAccommodations);
            model.addAttribute("accommodations", accommodations);
            model.addAttribute("success", false);
            model.addAttribute("message", e.getMessage());
            return "my-accommodation";
        }
    }
    
    @GetMapping("/cancel-reserved-accommodation")
    public String cancelReservedAccommodation(@RequestParam(name = "accommodationId") Long accommodationId, Model model) {
        try {
            accommodationService.cancelReservedAccommodation(accommodationId);
            List<Accommodation> accommodations = accommodationService.getUserAccomodations();
            List<Accommodation> reservedAccommodations = accommodationService.getUserReservedAccomodations();
            model.addAttribute("reservedAccommodations", reservedAccommodations);
            model.addAttribute("accommodations", accommodations);
            model.addAttribute("success", true);
            model.addAttribute("message", "Accommodation reservation cancel successfully");
            return "my-accommodation";

        } catch (Exception e) {
            List<Accommodation> accommodations = accommodationService.getUserAccomodations();
            List<Accommodation> reservedAccommodations = accommodationService.getUserReservedAccomodations();
            model.addAttribute("reservedAccommodations", reservedAccommodations);
            model.addAttribute("accommodations", accommodations);
            model.addAttribute("success", false);
            model.addAttribute("message", e.getMessage());
            return "my-accommodation";
        }
    }

    @GetMapping("/reserve-accommodation")
    public String reserveAccommodation(@RequestParam Long id, Model model) {
        accommodationService.reserveAccommodation(id);
        List<Accommodation> accommodations = accommodationService.getAvailableAccomodations();
        model.addAttribute("accommodations", accommodations);
        model.addAttribute("success", true);
        model.addAttribute("message", "Accommodation reserved successfully");
        return "accommodation";


    }

    @GetMapping("/my-market-place")
    public String myMarketPlace(Model model) {
        List<MarketPlace> marketPlaces = marketPlaceService.getUserMarketPlace();
        List<MarketPlace> reservedMarketPlaces = marketPlaceService.getUserReservedMarketPlace();
        model.addAttribute("reservedMarketPlaces", reservedMarketPlaces);
        model.addAttribute("marketPlaces", marketPlaces);
        model.addAttribute("success", "");
        model.addAttribute("message", "");

        return "my-market-place";
    }


    @PostMapping("/add-market-place")
    public String addMarketPlace(@ModelAttribute(name = "AddMarketPlaceForm") AddMarketPlaceForm addMarketPlaceForm,
                             @RequestParam("file") MultipartFile file,
                             Model model) {
        try {
            marketPlaceService.addMarketPlace(addMarketPlaceForm, file);
            List<MarketPlace> marketPlaces = marketPlaceService.getUserMarketPlace();
            List<MarketPlace> reservedMarketPlaces = marketPlaceService.getUserReservedMarketPlace();
            model.addAttribute("reservedMarketPlaces", reservedMarketPlaces);
            model.addAttribute("marketPlaces", marketPlaces);
            model.addAttribute("success", true);
            model.addAttribute("message", "Market places added/updated successfully");
            return "my-market-place";

        } catch (Exception e) {
            List<MarketPlace> marketPlaces = marketPlaceService.getUserMarketPlace();
            List<MarketPlace> reservedMarketPlaces = marketPlaceService.getUserReservedMarketPlace();
            model.addAttribute("reservedMarketPlaces", reservedMarketPlaces);
            model.addAttribute("marketPlaces", marketPlaces);
            model.addAttribute("success", false);
            model.addAttribute("message", e.getMessage());
            return "my-market-place";
        }
    }

    @GetMapping("/delete-market-place")
    public String deleteMarketPlace(@RequestParam(name = "marketPlaceId") Long marketPlaceId, Model model) {
        try {
            marketPlaceService.deleteMarketPlace(marketPlaceId);
            List<MarketPlace> marketPlaces = marketPlaceService.getUserMarketPlace();
            List<MarketPlace> reservedMarketPlaces = marketPlaceService.getUserReservedMarketPlace();
            model.addAttribute("reservedMarketPlaces", reservedMarketPlaces);
            model.addAttribute("marketPlaces", marketPlaces);
            model.addAttribute("success", true);
            model.addAttribute("message", "Market places deleted successfully");
            return "my-market-place";

        } catch (Exception e) {
            List<MarketPlace> marketPlaces = marketPlaceService.getUserMarketPlace();
            List<MarketPlace> reservedMarketPlaces = marketPlaceService.getUserReservedMarketPlace();
            model.addAttribute("reservedMarketPlaces", reservedMarketPlaces);
            model.addAttribute("marketPlaces", marketPlaces);
            model.addAttribute("success", false);
            model.addAttribute("message", e.getMessage());
            return "my-market-place";
        }
    }
    
    @GetMapping("/cancel-reserved-market-place")
    public String cancelReservedMarketPlace(@RequestParam(name = "marketPlaceId") Long marketPlaceId, Model model) {
        try {
            marketPlaceService.cancelReservedMarketPlace(marketPlaceId);
            List<MarketPlace> marketPlaces = marketPlaceService.getUserMarketPlace();
            List<MarketPlace> reservedMarketPlaces = marketPlaceService.getUserReservedMarketPlace();
            model.addAttribute("reservedMarketPlaces", reservedMarketPlaces);
            model.addAttribute("marketPlaces", marketPlaces);
            model.addAttribute("success", true);
            model.addAttribute("message", "Market places reservation cancel successfully");
            return "my-market-place";

        } catch (Exception e) {
            List<MarketPlace> marketPlaces = marketPlaceService.getUserMarketPlace();
            List<MarketPlace> reservedMarketPlaces = marketPlaceService.getUserReservedMarketPlace();
            model.addAttribute("reservedMarketPlaces", reservedMarketPlaces);
            model.addAttribute("marketPlaces", marketPlaces);
            model.addAttribute("success", false);
            model.addAttribute("message", e.getMessage());
            return "my-market-place";
        }
    }

    @GetMapping("/reserve-market-place")
    public String reserveMarketPlace(@RequestParam Long id, Model model) {

        marketPlaceService.reserveMarketPlace(id);
        List<MarketPlace> marketPlaces = marketPlaceService.getAvailableMarketPlaces();
        model.addAttribute("marketPlaces", marketPlaces);
        model.addAttribute("success", true);
        model.addAttribute("message", "Accommodation reserved successfully");
        return "market-place";
    }

    @GetMapping("/my-rides")
    public String myRides(Model model) {
        List<Ride> rides = rideService.getUserRides();
        List<Ride> reservedRides = rideService.getUserReservedRides();
        model.addAttribute("reservedRides", reservedRides);
        model.addAttribute("rides", rides);
        model.addAttribute("success", "");
        model.addAttribute("message", "");

        return "my-rides";
    }

    @PostMapping("/add-ride")
    public String addRide(@ModelAttribute(name = "AddRideForm") AddRideForm addRideForm,
                                 Model model) {
        try {
            rideService.addRide(addRideForm);
            List<Ride> rides = rideService.getUserRides();
            List<Ride> reservedRides = rideService.getUserReservedRides();
            model.addAttribute("reservedRides", reservedRides);
            model.addAttribute("rides", rides);
            model.addAttribute("success", true);
            model.addAttribute("message", "Ride added successfully");

            return "my-rides";

        } catch (Exception e) {
            List<Ride> rides = rideService.getUserRides();
            List<Ride> reservedRides = rideService.getUserReservedRides();
            model.addAttribute("reservedRides", reservedRides);
            model.addAttribute("rides", rides);
            model.addAttribute("success", false);
            model.addAttribute("message", e.getMessage());
            return "my-rides";
        }
    }

    @GetMapping("/delete-ride")
    public String deleteRide(@RequestParam(name = "rideId") Long rideId, Model model) {
        try {
            rideService.deleteRide(rideId);
            List<Ride> rides = rideService.getUserRides();
            List<Ride> reservedRides = rideService.getUserReservedRides();
            model.addAttribute("reservedRides", reservedRides);
            model.addAttribute("rides", rides);
            model.addAttribute("success", true);
            model.addAttribute("message", "Ride deleted successfully");

            return "my-rides";

        } catch (Exception e) {
            List<Ride> rides = rideService.getUserRides();
            List<Ride> reservedRides = rideService.getUserReservedRides();
            model.addAttribute("reservedRides", reservedRides);
            model.addAttribute("rides", rides);
            model.addAttribute("success", false);
            model.addAttribute("message", e.getMessage());
            return "my-rides";
        }
    }
    
    @GetMapping("/cancel-reserved-ride")
    public String cancelReservedRide(@RequestParam(name = "rideId") Long rideId, Model model) {
        try {
            rideService.cancelReservedRide(rideId);
            List<Ride> rides = rideService.getUserRides();
            List<Ride> reservedRides = rideService.getUserReservedRides();
            model.addAttribute("reservedRides", reservedRides);
            model.addAttribute("rides", rides);
            model.addAttribute("success", true);
            model.addAttribute("message", "Ride reservation cancel successfully");

            return "my-rides";

        } catch (Exception e) {
            List<Ride> rides = rideService.getUserRides();
            List<Ride> reservedRides = rideService.getUserReservedRides();
            model.addAttribute("reservedRides", reservedRides);
            model.addAttribute("rides", rides);
            model.addAttribute("success", false);
            model.addAttribute("message", e.getMessage());
            return "my-rides";
        }
    }

    @GetMapping("/book-ride")
    public String reserveRide(@RequestParam Long id, Model model) {

        rideService.reserveRide(id);
        List<Ride> rides = rideService.getAvailableRides();
        model.addAttribute("rides", rides);
        model.addAttribute("success", true);
        model.addAttribute("message", "Ride booked successfully");
        return "rides";
    }



    @GetMapping("/my-events")
    public String myEvents(Model model) {
        List<Event> events = eventService.getUserEvents();
        Set<Event> reservedEvents = eventService.getUserReservedEvents();
        model.addAttribute("reservedEvents", reservedEvents);
        model.addAttribute("events", events);
        model.addAttribute("success", "");
        model.addAttribute("message", "");

        return "my-events";
    }

    @PostMapping("/add-event")
    public String addEvent(@ModelAttribute(name = "AddEventForm") AddEventForm addEventForm,
                          Model model) {
        try {
            eventService.addEvent(addEventForm);
            List<Event> events = eventService.getUserEvents();
            Set<Event> reservedEvents = eventService.getUserReservedEvents();
            model.addAttribute("reservedEvents", reservedEvents);
            model.addAttribute("events", events);
            model.addAttribute("success", true);
            model.addAttribute("message", "Event added/updated successfully");

            return "my-events";

        } catch (Exception e) {
            List<Event> events = eventService.getUserEvents();
            Set<Event> reservedEvents = eventService.getUserReservedEvents();
            model.addAttribute("reservedEvents", reservedEvents);
            model.addAttribute("events", events);
            model.addAttribute("success", false);
            model.addAttribute("message", e.getMessage());
            return "my-events";
        }
    }

    @GetMapping("/book-event")
    public String reserveEvent(@RequestParam Long id, Model model) {

        eventService.reserveEvent(id);
        List<Event> events = eventService.getAvailableEvents();
        model.addAttribute("events", events);
        model.addAttribute("success", true);
        model.addAttribute("message", "Event reserved successfully");
        return "event";
    }

    @GetMapping("/delete-event")
    public String deleteEvent(@RequestParam(name = "eventId") Long eventId, Model model) {
        try {
            eventService.deleteEvent(eventId);
            List<Event> events = eventService.getUserEvents();
            Set<Event> reservedEvents = eventService.getUserReservedEvents();
            model.addAttribute("reservedEvents", reservedEvents);
            model.addAttribute("events", events);
            model.addAttribute("success", true);
            model.addAttribute("message", "Event deleted successfully");

            return "my-events";

        } catch (Exception e) {
            List<Event> events = eventService.getUserEvents();
            Set<Event> reservedEvents = eventService.getUserReservedEvents();
            model.addAttribute("reservedEvents", reservedEvents);
            model.addAttribute("events", events);
            model.addAttribute("success", false);
            model.addAttribute("message", e.getMessage());
            return "my-events";
        }
    }

    @GetMapping("/cancel-reserved-event")
    public String cancelReservedEvent(@RequestParam(name = "eventId") Long eventId, Model model) {
        try {
            eventService.cancelReservedEvent(eventId);
            List<Event> events = eventService.getUserEvents();
            Set<Event> reservedEvents = eventService.getUserReservedEvents();
            model.addAttribute("reservedEvents", reservedEvents);
            model.addAttribute("events", events);
            model.addAttribute("success", true);
            model.addAttribute("message", "Event reservation cancel successfully");

            return "my-events";

        } catch (Exception e) {
            List<Event> events = eventService.getUserEvents();
            Set<Event> reservedEvents = eventService.getUserReservedEvents();
            model.addAttribute("reservedEvents", reservedEvents);
            model.addAttribute("events", events);
            model.addAttribute("success", false);
            model.addAttribute("message", e.getMessage());
            return "my-events";
        }
    }

}