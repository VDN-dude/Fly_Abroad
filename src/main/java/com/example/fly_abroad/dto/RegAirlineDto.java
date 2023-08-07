package com.example.fly_abroad.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
public class RegAirlineDto {

    @Pattern(regexp = "([A-Za-z])*", message = "The First Name contains invalid characters")
    private String adminFirstName;

    @Pattern(regexp = "([A-Za-z])*", message = "The Last Name contains invalid characters")
    private String adminLastName;

    private String adminEmail;

    @Pattern(regexp = "[A-Za-z&.\\-\\d]*", message = "This company name contains invalid characters")
    private String companyName;

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

    @Pattern(regexp = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$", message = "This phone number contains invalid characters")
    private String officePhone;

    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$", message = "The password short or contain invalid characters")
    private String password;
}
