/**
 * 
 */
package com.gdpdemo.GDPSprint1Project;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.gdpdemo.GDPSprint1Project.Repository.EventRepository;
import com.gdpdemo.GDPSprint1Project.Repository.HomeRepository;

/**
 * @author Spurthi Ravula S559190
 */
@Controller
public class EventController {
	
	@Autowired
	private EventRepository eventRepository;
	
	 @GetMapping("/events")
	    public String events(Model model) {
	        // Get list of events (dummy data for demonstration)
	        //getEvents
		 //List<Event> events = eventRepository.findAll();
		 List<Event> events = getEvents();
	        System.out.println(events);
	        model.addAttribute("events", events);
	        return "events"; // Thymeleaf template name
	    }
	 
	 private List<Event> getEvents() {
		    List<Event> events = new ArrayList<>();
		    Random random = new Random();
		    for (int i = 0; i < 3; i++) { // Generating 3 random events
		        // Generating random date within the last year
		        long millisInYear = TimeUnit.DAYS.toMillis(365);
		        long randomMillisSinceEpoch = System.currentTimeMillis() - (long) (random.nextDouble() * millisInYear);
		        Date randomDate = new Date(randomMillisSinceEpoch);

		        events.add(new Event(i + 1, "Event " + (i + 1), randomDate, "Location " + (i + 1), 100 + i * 50));
		    }
		    return events;
		}
}
