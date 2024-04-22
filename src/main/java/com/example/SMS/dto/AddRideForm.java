package com.example.SMS.dto;

import lombok.Data;

@Data
public class AddRideForm {

    private Long id;
    private String title;
    private String description;
    private String from;
    private String to;
    private String dateTime;
    private Double price;
    private Integer availableSeats;


}
