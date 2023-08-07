package com.example.fly_abroad.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
public class PageableFlights {

    private List<Flight> flights;

    private Long countOfPages;

    private int page;

    private int size;
}
