package com.example.fly_abroad.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PageableBookedTicket {

    private List<BookedTicket> bookedTickets;

    private Long countOfPages;

    private int page;

    private int size;
}
