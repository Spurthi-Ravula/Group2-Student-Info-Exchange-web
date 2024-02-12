/**
 * 
 */
package com.gdpdemo.GDPSprint1Project;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * @author Spurthi Ravula S559190
 */
@Entity
@Table
public class Event {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	
	@NotBlank(message= "Event Name is required!")
	private String event_name;
	
	@NotBlank
	private Date event_date;
	
	@NotBlank
	private String location;
	
	@NotBlank
	private int alloted_seats;
	    

	public Event(int id, @NotBlank(message = "Event Name is required!") String event_name, @NotBlank java.util.Date randomDate,
			@NotBlank String location, @NotBlank int alloted_seats) {
		super();
		this.id = id;
		this.event_name = event_name;
		this.event_date = randomDate;
		this.location = location;
		this.alloted_seats = alloted_seats;
	}


	public int getId() {
		return id;
	}


	public String getEvent_name() {
		return event_name;
	}


	public Date getEvent_date() {
		return event_date;
	}


	public String getLocation() {
		return location;
	}


	public int getAlloted_seats() {
		return alloted_seats;
	}


	public void setId(int id) {
		this.id = id;
	}


	public void setEvent_name(String event_name) {
		this.event_name = event_name;
	}


	public void setEvent_date(Date event_date) {
		this.event_date = event_date;
	}


	public void setLocation(String location) {
		this.location = location;
	}


	public void setAlloted_seats(int alloted_seats) {
		this.alloted_seats = alloted_seats;
	}
	
	

}