package com.example.SMS.dto;

import lombok.Data;

@Data
public class AddAccommodationForm {

    private Long id;
    private String title;
    private String description;
    private String address;
    private Double price;
    private String availableFrom;

}
