package com.example.SMS.dto;

import lombok.Data;

@Data
public class AddMarketPlaceForm {

    private Long id;
    private String title;
    private String description;
    private String address;
    private Double price;


}
