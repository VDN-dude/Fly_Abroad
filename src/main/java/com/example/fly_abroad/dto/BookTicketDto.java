package com.example.fly_abroad.dto;

import com.example.fly_abroad.entity.TicketClass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BookTicketDto {

    private Long flightId;

    private TicketClass ticketClass;

    private int quantity;
}
