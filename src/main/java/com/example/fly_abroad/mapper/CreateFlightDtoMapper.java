package com.example.fly_abroad.mapper;

import com.example.fly_abroad.dto.CreateFlightDto;
import com.example.fly_abroad.dto.CreateTicketDto;
import com.example.fly_abroad.entity.Address;
import com.example.fly_abroad.entity.Airport;
import com.example.fly_abroad.entity.Flight;
import com.example.fly_abroad.entity.Ticket;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CreateFlightDtoMapper {

    public static Flight createFlightDtoToFlight(CreateFlightDto createFlightDto) {

        List<Ticket> tickets = new ArrayList<>();

        Address fromAddress = Address.builder()
                .country(createFlightDto.getFromCountry())
                .city(createFlightDto.getFromCity())
                .build();
        Airport from = Airport.builder()
                .name(createFlightDto.getFromName())
                .address(fromAddress)
                .build();

        Address toAddress = Address.builder()
                .country(createFlightDto.getToCountry())
                .city(createFlightDto.getToCity())
                .build();
        Airport to = Airport.builder()
                .name(createFlightDto.getToName())
                .address(toAddress)
                .build();

        LocalDateTime departure = LocalDateTime.parse(createFlightDto.getDeparture(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime arrival = LocalDateTime.parse(createFlightDto.getArrival(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        Flight flight = Flight.builder()
                .from(from)
                .to(to)
                .departure(departure)
                .arrival(arrival)
                .duration(Duration.between(departure, arrival))
                .build();

        int totalTicketsQuantity = 0;
        for (CreateTicketDto createTicketDto : createFlightDto.getTickets()) {
            Ticket ticket = Ticket.builder()
                    .ticketClass(createTicketDto.getTicketClass())
                    .price(createTicketDto.getPrice())
                    .totalQuantity(createTicketDto.getQuantity())
                    .bookedQuantity(0)
                    .unbookedQuantity(createTicketDto.getQuantity())
                    .flight(flight)
                    .build();
            tickets.add(ticket);
            totalTicketsQuantity = totalTicketsQuantity + ticket.getTotalQuantity();
        }

        flight.setTotalTicketsQuantity(totalTicketsQuantity);
        flight.setUnbookedTicketsQuantity(totalTicketsQuantity);
        flight.setTickets(tickets);
        return flight;
    }
}
