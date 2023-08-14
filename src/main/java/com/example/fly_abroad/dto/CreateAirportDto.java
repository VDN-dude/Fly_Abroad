package com.example.fly_abroad.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
public class CreateAirportDto {

    @Pattern(regexp = "[A-Za-z&.\\-\\d]*", message = "This company name contains invalid characters")
    private String name;

    @Pattern(regexp = "([A-Za-z])*", message = "This Country contains invalid characters")
    private String country;

    @Pattern(regexp = "^[A-Za-z\\d]+\\-?[A-Za-z\\d]*", message = "This City is incorrect")
    private String city;

    @Pattern(regexp = "^[A-Za-z\\d]+\\-?[A-Za-z\\d]*", message = "This Street is incorrect")
    private String street;

    @Pattern(regexp = "^[A-Za-z\\d]+\\-?[A-Za-z\\d]*", message = "This State is incorrect")
    private String state;

    @Pattern(regexp = "^\\d+[\\-\\d]+[A-Za-z]?[\\-\\d]*[A-Za-z]?", message = "This Building is incorrect")
    private String building;

    @Pattern(regexp = "^\\d+?[\\-\\d]{4,9}", message = "This zip is incorrect")
    private String zip;

    private String description;

}
