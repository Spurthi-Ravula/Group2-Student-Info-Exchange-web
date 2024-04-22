package com.example.SMS.dto;

import lombok.Data;

@Data
public class AddEventForm {

    private Long id;
    private String title;
    private String description;
    private String address;
    private int availableSeats;
    private String dateTime;
    private Double price;


}
