package com.example.fly_abroad.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
public class SearchFlightDto {

    @Pattern(regexp = "([A-Za-z])*", message = "The Country contains invalid characters")
    private String fromCity;

    @Pattern(regexp = "([A-Za-z])*", message = "The Country contains invalid characters")
    private String toCity;

    private String departureDay;

    private int ticketQuantity;
}
